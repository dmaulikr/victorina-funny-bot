package org.zella.actions.impl;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import org.zella.actions.AbstractBotAction;

/**
 * @author zella.
 */
public class LeaveBotAction extends AbstractBotAction {

    public LeaveBotAction(ActorRef bot, String botId) {
        super(bot, botId);
    }

    @Override
    public void execute() {
        bot.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }


}
