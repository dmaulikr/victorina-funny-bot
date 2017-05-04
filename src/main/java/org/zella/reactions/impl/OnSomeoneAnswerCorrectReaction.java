package org.zella.reactions.impl;

import akka.actor.ActorRef;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.zella.actions.IBotAction;
import org.zella.actions.impl.TellBotAction;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;
import org.zella.events.impl.AnsweredCorrectGameEvent;
import org.zella.reactions.OnGameEventBotReaction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zella.
 */
public class OnSomeoneAnswerCorrectReaction extends OnGameEventBotReaction<AnsweredCorrectGameEvent> {


    public OnSomeoneAnswerCorrectReaction(String botId, ActorRef botActor, AnsweredCorrectGameEvent gameEvent, IPhrasesDao dao, IConfig config) {
        super(botActor, botId, gameEvent, dao, config);
        init();
    }

    @Override
    protected IBotAction createActionFrom(AnsweredCorrectGameEvent gameEvent) {
        //example:  String template = "Welcome ${name}! Mame privet";

        String winnerName = gameEvent.getGameState().getPlayer(gameEvent.getWinner().getPlayerId()).getName();

        Map<String, String> data = new HashMap<>();
        data.put("name", winnerName);

        String phrase = phrasesDao.randomPhraseByType("someoneAnswerCorrect");
        String whatToTell = StrSubstitutor.replace(phrase, data);

        return new TellBotAction(botActor, botId, whatToTell);
    }
}
