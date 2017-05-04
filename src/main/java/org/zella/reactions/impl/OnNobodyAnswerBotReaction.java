package org.zella.reactions.impl;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;
import org.zella.actions.impl.TellBotAction;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;
import org.zella.events.impl.NobodyAnsweredGameEvent;
import org.zella.events.impl.PlayerJoinedGameEvent;
import org.zella.reactions.OnGameEventBotReaction;

/**
 * @author zella.
 */

public class OnNobodyAnswerBotReaction extends OnGameEventBotReaction<NobodyAnsweredGameEvent> {

    public OnNobodyAnswerBotReaction(String botId, ActorRef botActor, NobodyAnsweredGameEvent gameEvent, IPhrasesDao phrasesDao, IConfig config) {
        super(botActor, botId, gameEvent, phrasesDao, config);
        init();
    }

    @Override
    protected IBotAction createActionFrom(NobodyAnsweredGameEvent gameEvent) {

        final String phrase = phrasesDao.randomPhraseByType("nobodyAnswer");

        return new TellBotAction(botActor, botId, phrase);
    }

}
