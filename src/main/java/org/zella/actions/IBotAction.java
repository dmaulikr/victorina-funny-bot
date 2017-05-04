package org.zella.actions;

import akka.actor.ActorRef;

/**
 * Действие, которое бот делает. Действие должно быть атомарной операцией
 *
 * @author zella.
 */
public interface IBotAction {


    /**
     * @return актор бота
     */
    ActorRef getBot();

    String getBotId();

    /**
     * Немедленно выполнить
     */
    void execute();

}
