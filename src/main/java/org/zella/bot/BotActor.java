package org.zella.bot;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import org.slf4j.LoggerFactory;
import org.zella.actions.impl.TellBotAction;
import org.zella.config.IConfig;
import org.zella.config.ICredentials;
import org.zella.config.impl.BotCredentials;
import org.zella.db.IAnswersDao;
import org.zella.db.IBotDao;
import org.zella.db.IPhrasesDao;
import org.zella.events.IGameEvent;
import org.zella.events.impl.*;
import org.zella.game.GameEventInterpretator;
import org.zella.net.fromandroid.GameHttpClient;
import org.zella.net.fromandroid.IServerApi;
import org.zella.net.impl.WebSocketGameClient;
import org.zella.reactions.IBotReaction;
import org.zella.reactions.impl.*;
import org.zella.utils.Json;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import scala.concurrent.duration.Duration;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * Собственно сам бот
 *
 * @author zella.
 */
public class BotActor extends UntypedActor {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BotActor.class);

    private final IPhrasesDao phrasesDao;

    private final IAnswersDao answersDao;

    private final IConfig config;

    private final IServerApi httpApi = GameHttpClient.getApi();

    private final WebSocketGameClient wsClient;

    private final GameEventInterpretator gameEventInterpretator;

    //набор всех реакций бота на игровые события
//    private final Map<Class, IBotReaction> reactions;

    private final IBotDao.BotInfo botInfo;

    //TODO также надо смотреть что тут у нас
    private CancelableReaction currentReaction = CancelableReaction.empty();


    private BotActor(IPhrasesDao phrasesDao, IAnswersDao answersDao, IConfig config, GameEventInterpretator gameEventInterpretator, IBotDao.BotInfo botInfo) throws URISyntaxException {
        this.phrasesDao = phrasesDao;
        this.answersDao = answersDao;
        this.config = config;
        this.gameEventInterpretator = gameEventInterpretator;
        this.botInfo = botInfo;


        final String uri = "ws://" + config.getServerAddress() +
          "/game?email=" + botInfo.credentials.getEmail() + "&pass=" + botInfo.credentials.getPass();

        wsClient = new WebSocketGameClient(new URI(uri), self());
    }


    public static Props props(IPhrasesDao phrasesDao, IAnswersDao answersDao, IConfig config, GameEventInterpretator gameEventInterpretator, IBotDao.BotInfo botInfo) {
        return Props.create(BotActor.class, (Creator<BotActor>) () -> new BotActor(
          phrasesDao,
          answersDao,
          config,
          gameEventInterpretator,
          botInfo)
        );
    }


    @Override
    public void preStart() throws Exception {
        super.preStart();
        self().tell(botInfo.credentials, self());
    }

    @Override
    public void onReceive(Object msg) {
        // Сюда приходят игровые события, по ним мы выбираем реакцию

        log.trace("Receive message: {}", msg.toString());

        //строка - значит с сервака
        if (msg instanceof String) {
            //game event
            IGameEvent gameEvent = gameEventInterpretator.createFromServerMessage((String) msg);

            IBotReaction reaction = reactionFromGameEvent(gameEvent);
            if (reaction == null) return;

            //любой евент кроме подсказки , инфы при коннекте и "никто не ответил" прерывает задуманное ботом
            //т.е в идеале дейсвие юзера должно прерывать задуманное ботом. А у задуманного в свою очередь есть
            //задержка. Во время этой задержи можно дейсвие отменить. Травля шуток работает на этом принципе
            if (
              !(gameEvent instanceof HintGameEvent)
                &&
                !(gameEvent instanceof NobodyAnsweredGameEvent)
                &&
                !(gameEvent instanceof GetAllInfoWhenJoinGameEvent)
                //TODO может учитывать других ботов?
                //если это наше болабольство, то тоже не учитывается
                && !(gameEvent instanceof MessageInChatGameEvent && ((MessageInChatGameEvent) gameEvent).getPlayerId().endsWith(botInfo.botId))
              )
                currentReaction.cancel();
            currentReaction = new CancelableReaction(
              reaction.react().subscribe()
              , reaction
            );
            log.debug("step 2 call react {}",reaction.getClass().getSimpleName());
            //информируем насяльника о смене состояния игры, да каждый бот информирует, ну бывает :)
            context().parent().tell(gameEvent.getGameState(), self());
            //бот говорит в чат
        } else if (msg instanceof TellBotAction) {
            //3 шаг должен быть после 2ого и делея реакции
            log.debug("step 3 receive tell: "+((TellBotAction) msg).getWhatToTell());
            wsClient.send(((TellBotAction) msg).getWhatToTellAsJson());

            //бот думает, что неплохо бы шуткануть
        } else if (msg instanceof JokePlease) {
            IBotReaction reaction = new TravlyaJokeBotReaction(getId(), self(), phrasesDao, config);
            currentReaction.cancel();
            currentReaction = new CancelableReaction(reaction.react().subscribe(), reaction);
        } else if (msg instanceof BotCredentials) {
            //we don't handle result, so we don't breaks actor contract, it's ok
            httpApi.loginByEmailPass(((ICredentials) msg).getEmail(), ((ICredentials) msg).getPass())
              .subscribeOn(Schedulers.io())
              .toBlocking().subscribe();
            try {
                wsClient.connectBlocking();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            initSchedulers();

        } else if (msg instanceof PingPlease) {
            String pingMsg = Json.newObject().put("type", "ping").toString();
            wsClient.send(pingMsg);
        } else if (msg instanceof MaybeLeave) {
            new MaybeLeaveBotReaction(getId(), self(), phrasesDao, config).react()
              .subscribe();
        }

        unhandled(msg);
    }

    @Nullable
    private IBotReaction reactionFromGameEvent(IGameEvent gameEvent) {

        if (gameEvent instanceof HintGameEvent) {
            return new OnHintBotReaction(botInfo.botId, self(), (HintGameEvent) gameEvent, phrasesDao, config, answersDao);
        } else if (gameEvent instanceof AnsweredCorrectGameEvent) {
            return new OnSomeoneAnswerCorrectReaction(botInfo.botId, self(), (AnsweredCorrectGameEvent) gameEvent, phrasesDao, config);
        } else if (gameEvent instanceof NewQuestionGameEvent) {
            return new OnNewQuestionBotReaction(botInfo.botId, self(), (NewQuestionGameEvent) gameEvent, phrasesDao, config);
        } else if (gameEvent instanceof GetAllInfoWhenJoinGameEvent) {
            return new OnReceiveGameStateWhenJoinBotReaction(botInfo.botId, self(), (GetAllInfoWhenJoinGameEvent) gameEvent, phrasesDao, config);
        } else if (gameEvent instanceof PlayerJoinedGameEvent) {
            return new OnPlayerJoinedBotReaction(botInfo.botId, self(), (PlayerJoinedGameEvent) gameEvent, phrasesDao, config);
        } else if (gameEvent instanceof NobodyAnsweredGameEvent) {
            return new OnNobodyAnswerBotReaction(botInfo.botId, self(), (NobodyAnsweredGameEvent) gameEvent, phrasesDao, config);
        }


        log.warn("Not implemented game event: {}", gameEvent.getName());
        return null;
    }

    private void initSchedulers() {
        //по-хорошему надо через became() делать

        //травим шутки
        initScheduler(config.getDelayFor("jokeScheduler").max, config.getDelayFor("jokeScheduler").max, new JokePlease());

        //пинг
        initScheduler(config.getPingPeriod(), config.getPingPeriod(), new PingPlease());
//        initScheduler(10000, 10000, new PingPlease());

        //иногда бот хочет стать ливером
        initScheduler(config.getDelayFor("leaveScheduler").max, config.getDelayFor("leaveScheduler").max, new MaybeLeave());
    }

    private void initScheduler(long initialDelay, long period, Object message) {
        context().system().scheduler().schedule(
          Duration.create(initialDelay, TimeUnit.MILLISECONDS),
          Duration.create(period, TimeUnit.MILLISECONDS),
          self(),
          message,
          context().dispatcher(), ActorRef.noSender()
        );
    }


    private String getId() {
        return botInfo.botId;
    }

    @Override
    public void postStop() throws Exception {
        log.info("Bot stoped");
        super.postStop();
        wsClient.closeBlocking();
    }

    private class JokePlease {
    }

    private class PingPlease {
    }

    private class MaybeLeave {
    }

    private static class CancelableReaction {
        public final Subscription subscription;
        public final IBotReaction reaction;

        CancelableReaction(Subscription subscription, IBotReaction reaction) {
            this.subscription = subscription;
            this.reaction = reaction;
        }

        public static CancelableReaction empty() {
            return new CancelableReaction(Subscriptions.empty(), null);
        }

        public void cancel() {
            if (subscription != null)
                subscription.unsubscribe();
        }
    }

}
