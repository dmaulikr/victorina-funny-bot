package org.zella.events.impl;

import org.zella.game.fromandroid.GameState;
import org.zella.events.AbstactGameEvent;

/**
 * @author zella.
 */
public class MessageInChatGameEvent extends AbstactGameEvent {

    private final String playerId;
    private final String text;

    public MessageInChatGameEvent(GameState gameState, String playerId, String text ) {
        super(gameState);
        this.playerId = playerId;
        this.text = text;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getText() {
        return text;
    }
}
