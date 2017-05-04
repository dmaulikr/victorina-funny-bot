package org.zella.events;

import org.zella.game.fromandroid.GameState;

/**
 * Событие, которое случается в игре, собственно мы узнаем
 * о нем по сообщению с сервера
 *
 * Ну и состояние игры после события
 *
 * @author zella.
 */
public interface IGameEvent {

    /**
     * @return новое игровое состояние
     */
    GameState getGameState();

    String getName();

}
