package org.zella.supervisor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import org.slf4j.LoggerFactory;
import org.zella.bot.BotActor;
import org.zella.config.IConfig;
import org.zella.db.IAnswersDao;
import org.zella.db.IBotDao;
import org.zella.db.IPhrasesDao;
import org.zella.game.GameEventInterpretator;
import org.zella.game.fromandroid.GameState;
import org.zella.utils.Utils;
import scala.collection.JavaConverters;
import scala.concurrent.duration.Duration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zella.
 */
public class BotSupervisorActor extends UntypedActor {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BotSupervisorActor.class);

    private final IConfig config;

    private GameState gameState;

    private final IPhrasesDao phrasesDao;
    private final IAnswersDao answersDao;

    private final IBotDao botDao;

    public BotSupervisorActor(IConfig config, IPhrasesDao phrasesDao, IAnswersDao answersDao, IBotDao botDao) {
        this.config = config;
        this.phrasesDao = phrasesDao;
        this.answersDao = answersDao;
        this.botDao = botDao;
    }

    public static Props props(IConfig config, IPhrasesDao phrasesDao, IAnswersDao answersDao, IBotDao botDao) {
        return Props.create(BotSupervisorActor.class, (Creator<BotSupervisorActor>) () -> new BotSupervisorActor(
          config,
          phrasesDao,
          answersDao,
          botDao
        ));
    }


    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("start joinScheduler");
        context().system().scheduler().schedule(
          Duration.create(config.getDelayFor("joinSchedulerStart").max, TimeUnit.MILLISECONDS),
          Duration.create(config.getDelayFor("joinSchedulerPeriod").max, TimeUnit.MILLISECONDS),
          self(),
          new RandomEvent(Event.JOIN),
          context().dispatcher(), ActorRef.noSender()
        );
    }

    @Override
    public void onReceive(Object m) throws Throwable {
        if (m instanceof RandomEvent) {

            if (gameState == null) {
                log.info("Nobody tell me game state, break processing...");
                //ждем, потому учащаем
                //TODO тут лучше конечно become() но пох пока что, нет веремени
                context().system().scheduler().scheduleOnce(
                  Duration.create(config.getDelayFor("joinSchedulerStart").max, TimeUnit.MILLISECONDS), self(),
                  new RandomEvent(Event.JOIN),
                  context().dispatcher(), ActorRef.noSender());

                if (getBots().size() == 0) {
                    joinNewBot();
                }
                return;
            }
            if (((RandomEvent) m).event.equals(Event.JOIN)) {

                float percentToJoin =
                  percentToJoinBasedOnBotsAndPlayerCount(
                    getBots().size(),
                    gameState.getPlayers().size()
                  );

                if (Utils.shouldHappens(percentToJoin)) {
                    joinNewBot();
                }

            }
        } else if (m instanceof GameState) {
            this.gameState = (GameState) m;
        }
    }

    /**
     * @param bots               кол-во ботов в комнате
     * @param playersIncludeBots кол-во игроков в комнате
     * @return шанс на создание бота
     */
    private float percentToJoinBasedOnBotsAndPlayerCount(int bots, int playersIncludeBots) {

        int maxBots = config.getValue("maxBots").intValue();
        int maxUsersInRoom = config.getValue("maxUsersInRoom").intValue();

        //4 / 4 = 1.0    1 / 4 = 0.25
        float botsPayload = (float) bots / (float) maxBots;

        //4 / 4 = 1.0    1 / 4 = 0.25
        float inRoomPayload = (float) playersIncludeBots / (float) maxUsersInRoom;


        /*

       1 - (1+1) / 2 = 0;

       1 - (0+0) / 2 = 1;

       1- (0.5+0,5) /2 = 0.5


       //допустим макс 4 бота, макс 8 юзеров. В игре четверо человек

    1 - (0f + 0.5f) / 2f
1 - (1f/4f + 5f/8f) / 2f
1 - (2f/4f + 6f/8f) / 2f
1 - (3f/4f + 7f/8f) / 2f
1 - (4f/4f + 8f/8) / 2f

res0: Float = 0.75
res1: Float = 0.5625
res2: Float = 0.375
res3: Float = 0.1875
res4: Float = 0.0
         */
        float chance = 1f - (botsPayload + inRoomPayload) / 2f;
        if (bots == maxBots) chance = 0.0f;

        log.debug("Payloads [bots {} all {} maxBots {} maxUsers {}]", botsPayload, inRoomPayload, maxBots, maxUsersInRoom);
        log.debug("Counts [bots {} all {} joinChance {}]", bots, playersIncludeBots, chance);
        return chance;
    }

    private void joinNewBot() {

        Optional<String> botToJoin = config.selectRandomBotExcept(getBotsIds());

        if (!botToJoin.isPresent()) {
            log.warn("Can't select bot, no bots available");
            return;
        }

        IBotDao.BotInfo botInfo = botDao.findBy(config.getAvailableBots().get(botToJoin.get()));

        ActorRef bot = getContext().actorOf(BotActor.props(
          phrasesDao,
          answersDao,
          config,
          new GameEventInterpretator(),
          botInfo
        ), botInfo.botId);
//        getContext().watch(bot);
        log.info("bot created: {}", botInfo.toString());

    }

    /**
     * Боты сами ливают, мамка там покушать позвала и т д.
     */


    private Set<ActorRef> getBots() {
        return new HashSet<>(JavaConverters.asJavaCollectionConverter(context().children()).asJavaCollection());
    }


    private Set<String> getBotsIds() {
        Set<String> actors = getBots().stream().map(actorRef -> actorRef.path().name()).collect(Collectors.toSet());
        log.debug("childs:");
        actors.forEach(n -> log.debug(n + " "));
        log.debug("childs end");
        return actors;
    }


    @Override
    public SupervisorStrategy supervisorStrategy() {
        return SupervisorStrategy.stoppingStrategy();
    }

    @Override
    public void postStop() throws Exception {
        log.info("Supervisor stoped");
        super.postStop();
    }

    private class RandomEvent {

        final Event event;

        RandomEvent(Event event) {
            this.event = event;
        }
    }

    private enum Event {JOIN}
}
