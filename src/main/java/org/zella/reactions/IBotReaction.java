package org.zella.reactions;

import akka.actor.ActorRef;
import org.zella.actions.IBotAction;
import rx.Observable;

/**
 * Реакция бота на какое-нибудь действие, время реакции бывает разное :)
 *
 * @author zella.
 */
public interface IBotReaction {

    String getBotId();

    ActorRef getBotActor();

    /**
     * @return Время реагирования в мс
     */
    long getDelay();

    /**
     * @return шанс шуткана, или что этот нигер там себе позволяет. между [0 и 1]
     */
    float getActionChance();


    /**
     * Заставляем бота реагировать. Не забываем подписаться
     *
     * @return действие бота, которое было инициировано
     */
    Observable<IBotAction> react();

}
