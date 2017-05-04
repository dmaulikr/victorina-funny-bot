package org.zella.reactions;

import akka.actor.ActorRef;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;

/**
 * Реакция, которой нужна бд
 *
 * @author zella.
 */
public abstract class DbBotReaction extends AbstractBotReaction {

    protected final IPhrasesDao phrasesDao;

    public DbBotReaction(ActorRef botActor, String botId, IPhrasesDao phrasesDao, IConfig config) {
        super(botActor, botId, config);
        this.phrasesDao = phrasesDao;
    }


}
