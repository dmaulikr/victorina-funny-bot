package org.zella.reactions.impl;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;
import org.zella.actions.impl.TellBotAction;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;
import org.zella.events.impl.NewQuestionGameEvent;
import org.zella.reactions.OnGameEventBotReaction;

/**
 * @author zella.
 */
public class OnNewQuestionBotReaction extends OnGameEventBotReaction<NewQuestionGameEvent> {


    public OnNewQuestionBotReaction(String botId, ActorRef botActor, NewQuestionGameEvent gameEvent, IPhrasesDao phrasesDao, IConfig config) {
        super(botActor, botId, gameEvent, phrasesDao, config);
        init();
    }

    @Override
    protected IBotAction createActionFrom(NewQuestionGameEvent gameEvent) {

        String whatToTell = phrasesDao.randomPhraseByType("newQuestion");

        return new TellBotAction(botActor, botId, whatToTell);
    }



}
