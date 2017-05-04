package org.zella.events;

import org.zella.game.fromandroid.GameState;

import java.util.Objects;

/**
 * @author zella.
 */
public abstract class AbstactGameEvent implements IGameEvent {

    private final GameState gameState;

    protected AbstactGameEvent(GameState gameState) {
        this.gameState = gameState;
    }


    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstactGameEvent that = (AbstactGameEvent) o;
        return Objects.equals(getName(), getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
