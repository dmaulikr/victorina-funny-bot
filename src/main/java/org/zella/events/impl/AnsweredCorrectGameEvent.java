package org.zella.events.impl;

import org.zella.game.fromandroid.GameState;
import org.zella.game.fromandroid.Winner;
import org.zella.events.AbstactGameEvent;

/**
 * @author zella.
 */
public class AnsweredCorrectGameEvent extends AbstactGameEvent {
    private final String answer;
    private final Winner winner;

    public AnsweredCorrectGameEvent(GameState gameState, String answer, Winner winner, boolean isGameEnded) {
        super(gameState);
        this.answer = answer;
        this.winner = winner;
    }

    public String getAnswer() {
        return answer;
    }

    public Winner getWinner() {
        return winner;
    }
}
