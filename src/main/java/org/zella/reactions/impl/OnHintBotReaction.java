package org.zella.reactions.impl;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;
import org.zella.actions.impl.TellBotAction;
import org.zella.config.IConfig;
import org.zella.db.IAnswersDao;
import org.zella.db.IPhrasesDao;
import org.zella.events.impl.HintGameEvent;
import org.zella.reactions.OnGameEventBotReaction;
import org.zella.utils.Utils;

/**
 * @author zella.
 */

public class OnHintBotReaction extends OnGameEventBotReaction<HintGameEvent> {

    private final IAnswersDao answersDao;

    public OnHintBotReaction(String botId, ActorRef botActor, HintGameEvent gameEvent, IPhrasesDao phrasesDao, IConfig config, IAnswersDao answersDao) {
        super(botActor, botId, gameEvent, phrasesDao, config);
        this.answersDao = answersDao;
        init();
    }

    @Override
    protected IBotAction createActionFrom(HintGameEvent gameEvent) {

        //либо ответить правильно,
        // либо сказать какую-нибудь хуйню

        float chanceAnswerCorrect = config.getChanceFor("correctAnswer");

        String whatToTell = Utils.shouldHappens(chanceAnswerCorrect)
          ? answersDao.findAnswer(gameEvent.getGameState().getQuestion())
          : phrasesDao.randomPhraseByType("onHint");


        return new TellBotAction(botActor, botId, whatToTell);
    }

}
