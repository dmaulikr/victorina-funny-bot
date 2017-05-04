package org.zella.reactions.composite;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;
import org.zella.config.IConfig;
import org.zella.db.IPhrasesDao;
import org.zella.reactions.IBotReaction;
import org.zella.utils.Utils;
import rx.Observable;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Цепочка реакций бота
 * <p>
 * Должна состоять из {@link  org.zella.reactions.composite.ChildBotReaction}
 * <p>
 * Шанс выполнения дочерних реакций всегда 100%,
 * так что выполнения только части цепочки запрещено
 *
 * @author zella.
 */
public abstract class CompositeBotReaction implements IBotReaction {

    protected final IPhrasesDao phrasesDao;

    protected final List<ChildBotReaction> childReactions;

    protected final String botId;

    protected final ActorRef botActor;

    protected final IConfig config;

    protected CompositeBotReaction(String botId, ActorRef botActor, IConfig config, IPhrasesDao dao) {
        this.config = config;
        this.botId = botId;
        this.botActor = botActor;
        this.phrasesDao = dao;
        this.childReactions = initReactions();
    }

    protected abstract List<ChildBotReaction> initReactions();

    @Override
    public ActorRef getBotActor() {
        return botActor;
    }

    @Override
    public long getDelay() {
        //not tested
        return childReactions.stream().mapToLong(IBotReaction::getDelay).sum();
    }


    @Override
    public Observable<IBotAction> react() {

        if (Utils.shouldHappens(getActionChance())) {
            final List<Observable<IBotAction>> childrenReactions = childReactions
              .stream()
              .map(IBotReaction::react)
              .collect(Collectors.toList());
            return Observable.merge(childrenReactions);
        } else
            return Observable.timer(getDelay(), TimeUnit.MILLISECONDS)
              .map(aLong -> new CompositeNotHappenAction(getBotActor(), getBotId()));
    }


}
