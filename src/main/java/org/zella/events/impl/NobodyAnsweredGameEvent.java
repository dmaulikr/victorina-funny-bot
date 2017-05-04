package org.zella.events.impl;

import org.zella.game.fromandroid.GameState;
import org.zella.events.AbstactGameEvent;

/**
 * Никто не ответил, дурачье
 *
 * @author zella.
 */
public class NobodyAnsweredGameEvent extends AbstactGameEvent {

    private final String answer;

    public NobodyAnsweredGameEvent(GameState gameState, String answer) {
        super(gameState);
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

}
