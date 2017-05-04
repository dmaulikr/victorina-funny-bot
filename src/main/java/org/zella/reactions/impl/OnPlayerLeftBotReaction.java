package org.zella.reactions.impl;

import akka.actor.ActorRef;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.zella.actions.IBotAction;
import org.zella.actions.impl.TellBotAction;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;
import org.zella.events.impl.PlayerLeavedGameEvent;
import org.zella.reactions.OnGameEventBotReaction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zella.
 */

public class OnPlayerLeftBotReaction extends OnGameEventBotReaction<PlayerLeavedGameEvent> {

    public OnPlayerLeftBotReaction(String botId, ActorRef botActor, PlayerLeavedGameEvent gameEvent, IPhrasesDao phrasesDao, IConfig config) {
        super(botActor, botId, gameEvent, phrasesDao, config);
        init();
    }

    @Override
    protected IBotAction createActionFrom(PlayerLeavedGameEvent gameEvent) {

        Map<String, String> data = new HashMap<>();
        data.put("name", gameEvent.getUser().getName());

        final String phrase = phrasesDao.randomPhraseByType("playerLeft");

        String whatToTell = StrSubstitutor.replace(phrase, data);

        return new TellBotAction(botActor, botId, whatToTell);

    }

}
