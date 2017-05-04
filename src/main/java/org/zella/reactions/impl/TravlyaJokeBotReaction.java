package org.zella.reactions.impl;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;
import org.zella.actions.impl.TellBotAction;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;
import org.zella.reactions.DbBotReaction;

/**
 * Травим шутку, эта реакция не на игру, это просто в мозгу бота всякая реакция
 *
 * @author zella.
 */
public class TravlyaJokeBotReaction extends DbBotReaction {

    public TravlyaJokeBotReaction(String botId, ActorRef botActor, IPhrasesDao dao, IConfig config) {
        super(botActor, botId, dao, config);
        init();
    }


    @Override
    protected IBotAction createAction() {
        return new TellBotAction(botActor, botId,
          phrasesDao.randomPhraseByType("joke"));
    }
}
