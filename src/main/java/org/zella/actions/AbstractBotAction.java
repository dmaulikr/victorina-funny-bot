package org.zella.actions;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;

/**
 *
 * Содержит реализация с актором бота
 *
 * @author zella.
 */
public abstract class AbstractBotAction implements IBotAction {

    protected final ActorRef bot;

    protected final String botId;

    protected AbstractBotAction(ActorRef bot, String botId) {
        this.bot = bot;
        this.botId = botId;
    }

    @Override
    public ActorRef getBot() {
        return bot;
    }

    @Override
    public String getBotId() {
        return botId;
    }

}
