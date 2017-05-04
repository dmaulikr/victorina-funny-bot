package org.zella.reactions.impl;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;
import org.zella.actions.impl.TellBotAction;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;
import org.zella.events.impl.GetAllInfoWhenJoinGameEvent;
import org.zella.events.impl.NewQuestionGameEvent;
import org.zella.reactions.OnGameEventBotReaction;

/**
 *
 * Приветствие
 *
 * @author zella.
 */
public class OnReceiveGameStateWhenJoinBotReaction extends OnGameEventBotReaction<GetAllInfoWhenJoinGameEvent> {


    public OnReceiveGameStateWhenJoinBotReaction(String botId, ActorRef botActor, GetAllInfoWhenJoinGameEvent gameEvent, IPhrasesDao phrasesDao, IConfig config) {
        super(botActor, botId, gameEvent, phrasesDao, config);
        init();
    }

    @Override
    protected IBotAction createActionFrom(GetAllInfoWhenJoinGameEvent gameEvent) {

        String whatToTell = phrasesDao.randomPhraseByType("hello");

        return new TellBotAction(botActor, botId, whatToTell);
    }



}
