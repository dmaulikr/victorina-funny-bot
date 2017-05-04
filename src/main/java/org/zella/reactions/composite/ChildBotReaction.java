package org.zella.reactions.composite;

import akka.actor.ActorRef;
import org.zella.config.IConfig;
import org.zella.reactions.AbstractBotReaction;

/**
 * Составная часть Композитной реакции,
 * всегда 100%, потому что шанс определяется композитной реакцией
 *
 * @author zella.
 */
public abstract class ChildBotReaction extends AbstractBotReaction {

    protected ChildBotReaction(ActorRef botActor, String botId, IConfig config) {
        super(botActor, botId, config);
        init();
    }

    @Override
    public float getActionChance() {
        return 1.0f;
    }

}
