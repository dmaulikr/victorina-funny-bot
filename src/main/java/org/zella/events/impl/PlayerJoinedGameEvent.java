package org.zella.events.impl;

import org.zella.events.AbstactGameEvent;
import org.zella.game.fromandroid.GameState;
import org.zella.game.fromandroid.GameUser;

/**
 * @author zella.
 */
public class PlayerJoinedGameEvent extends AbstactGameEvent {

    private final GameUser gameUser;

    public PlayerJoinedGameEvent(GameState gameState, GameUser gameUser) {
        super(gameState);
        this.gameUser = gameUser;
    }

    public GameUser getUser() {
        return gameUser;
    }
}
