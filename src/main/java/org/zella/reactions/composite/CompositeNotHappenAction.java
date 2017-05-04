package org.zella.reactions.composite;

import akka.actor.ActorRef;
import org.zella.actions.AbstractBotAction;


/**
 * Специальное действие показывающее, что ничего не случилось в композитной реакции
 */
//TODO deprecated?
public class CompositeNotHappenAction extends AbstractBotAction {


    protected CompositeNotHappenAction(ActorRef bot, String botId) {
        super(bot, botId);
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException();
    }
}
