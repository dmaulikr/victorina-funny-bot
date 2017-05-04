package org.zella.events.impl;

import org.zella.game.fromandroid.GameState;
import org.zella.events.AbstactGameEvent;

/**
 * Ну что, начнем пожалуй
 *
 * @author zella.
 */
public class GetAllInfoWhenJoinGameEvent extends AbstactGameEvent {
    public GetAllInfoWhenJoinGameEvent(GameState gameState) {
        super(gameState);
    }
}
