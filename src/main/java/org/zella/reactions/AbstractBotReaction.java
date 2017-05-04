package org.zella.reactions;

import akka.actor.ActorRef;
import org.slf4j.LoggerFactory;
import org.zella.actions.IBotAction;
import org.zella.config.IConfig;
import org.zella.utils.Utils;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Одиночная реакция бота, базовая имплементация с актором
 *
 * @author zella.
 */
public abstract class AbstractBotReaction implements IBotReaction {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AbstractBotReaction.class);

    protected final String botId;

    protected IBotAction botAction;

    protected final ActorRef botActor;

    protected final IConfig config;

    protected AbstractBotReaction(ActorRef botActor, String botId, IConfig config) {
        this.botId = botId;
        this.botActor = botActor;
        this.config = config;
    }

    /**
     * should call this after all constructors
     */
    protected void init() {
        botAction = createAction();
    }

    //TODO надо в тестовом режиме делать все
    private Observable<IBotAction> createActionAsync() {
        final Observable<IBotAction> obs = Observable.fromCallable(this::createAction)
          .subscribeOn(Schedulers.io());
        return obs;
    }

    @Override
    public long getDelay() {
        String className = this.getClass().getSimpleName();

        if (className.isEmpty())
            throw new IllegalStateException("If you have anonymous class or composite, you should implement getDelay and getActionChance manually");

        return config.getDelayFor(this.getClass().getSimpleName()).rand();
    }

    @Override
    public float getActionChance() {
        String className = this.getClass().getSimpleName();

        if (className.isEmpty())
            throw new IllegalStateException("If you have anonymous class composite, you should implement getDelay and getActionChance manually");

        return config.getChanceFor(className);
    }

    @Override
    public ActorRef getBotActor() {
        return botActor;
    }

    @Override
    public String getBotId() {
        return botId;
    }

    /**
     * Инициализация действия. Долгие операции, например загрузка фразы
     * из базы должны быть выполнены здесь
     *
     * @return
     */
    protected abstract IBotAction createAction();

    @Override
    public Observable<IBotAction> react() {

        log.debug("Delay: {} for react {}", getDelay(), getClass().getSimpleName());

        Observable<Long> delay =
          Observable.timer(getDelay(), TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io());

        return Observable.zip(createActionAsync(), delay, (botAction1, aLong) -> {
            log.trace("Timer: {} Action: {}", aLong, botAction1);
            //TODO percent here
            if (Utils.shouldHappens(getActionChance())) {
                log.debug("Action execute: {}", botAction1);
                botAction1.execute();
            } else {
                log.debug("Action not execute: {}", botAction1);
                //also it breaks chain in CompositeReaction
//                throw new NotHappenException(this);
            }
            //TODO also should returns happen or not
            return botAction1;
        });

    }

    @Override
    public String toString() {
        return "AbstractBotReaction: " + getBotId() + " " + getDelay() + " " + getActionChance();
    }
}
