package org.zella.actions;

import org.zella.actions.IBotAction;

import java.util.List;

/**
 * ну тут все понятно. типо чтобы несколько действий
 *
 * Только надо-ли это? Думаю нет, если без делея, то можно просто запилить обычную action
 * А если с делеем, то тут уже реакция
 * @author zella.
 */
@Deprecated
public abstract class CompositeAction implements IBotAction {


    private final List<IBotAction> mChildActions;

    protected CompositeAction(List<IBotAction> mChildActions) {
        this.mChildActions = mChildActions;
    }

    @Override
    public void execute() {
        for (IBotAction action : mChildActions) {
            action.execute();
        }
    }


}
