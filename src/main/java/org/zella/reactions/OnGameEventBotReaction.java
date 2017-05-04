package org.zella.reactions;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;
import org.zella.events.IGameEvent;
import org.zella.events.impl.HintGameEvent;
import org.zella.reactions.impl.OnHintBotReaction;

/**
 * Реакция бота на игровое событие
 *
 * @author zella.
 */
public abstract class OnGameEventBotReaction<T extends IGameEvent> extends DbBotReaction {


    protected final T gameEvent;

    protected OnGameEventBotReaction(ActorRef botActor, String botId, T gameEvent, IPhrasesDao dao, IConfig config) {
        super(botActor, botId, dao, config);
        this.gameEvent = gameEvent;
    }


    public T getGameEvent() {
        return gameEvent;
    }

    @Override
    protected IBotAction createAction() {
        return createActionFrom(gameEvent);
    }

    protected abstract IBotAction createActionFrom(T gameEvent);



}
