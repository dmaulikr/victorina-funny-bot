package org.zella.events.impl;

import org.zella.game.fromandroid.GameState;
import org.zella.events.AbstactGameEvent;

/**
 * @author zella.
 */
public class NewQuestionGameEvent extends AbstactGameEvent {
    private final String question;

    public NewQuestionGameEvent(GameState gameState, String question) {
        super(gameState);
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}
