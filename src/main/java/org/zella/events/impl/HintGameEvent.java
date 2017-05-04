package org.zella.events.impl;

import org.apache.commons.lang3.StringUtils;
import org.zella.events.AbstactGameEvent;
import org.zella.game.fromandroid.GameState;

/**
 * Пришла подсказка
 *
 * @author zella.
 */
public class HintGameEvent extends AbstactGameEvent {

    private final String hint;

    public HintGameEvent(GameState gameState, String hint) {
        super(gameState);
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public int lettersNotOpened() {

        int count = StringUtils.countMatches("hint", "*");

        return count;

    }
}
