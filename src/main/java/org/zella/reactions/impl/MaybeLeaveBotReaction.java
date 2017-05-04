package org.zella.reactions.impl;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;
import org.zella.actions.impl.LeaveBotAction;
import org.zella.actions.impl.TellBotAction;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;
import org.zella.reactions.composite.ChildBotReaction;
import org.zella.reactions.composite.CompositeBotReaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Мамка кушать позвала. Или нет?
 *
 * @author zella.
 */
//FIXME bad inheritance
public class MaybeLeaveBotReaction extends CompositeBotReaction {


    public MaybeLeaveBotReaction(String botId, ActorRef botActor, IPhrasesDao phrasesDao, IConfig config) {
        super(botId, botActor, config, phrasesDao);

    }

    @Override
    protected List<ChildBotReaction> initReactions() {
        List<ChildBotReaction> childReactions = new ArrayList<>();

        childReactions.add(new ChildBotReaction(botActor, botId, config) {
            @Override
            protected IBotAction createAction() {

                final String leavePhrase = phrasesDao.randomPhraseByType("leave");

                return new TellBotAction(botActor, botId, leavePhrase);
            }

            @Override
            public long getDelay() {
                return config.getDelayFor("reactionChildLeavePhrase").rand();
            }

        });

        childReactions.add(new ChildBotReaction(botActor, botId, config) {
            @Override
            protected IBotAction createAction() {
                return new LeaveBotAction(botActor, botId);
            }

            @Override
            public long getDelay() {
                return config.getDelayFor("reactionChildLeave").rand();
            }

        });

        return childReactions;

    }


    @Override
    public String getBotId() {
        return botId;
    }

    @Override
    public float getActionChance() {
        return config.getChanceFor("reactionLeave");
    }
}
