package org.zella.actions.impl;

import akka.actor.ActorRef;
import com.google.gson.JsonObject;
import org.zella.actions.AbstractBotAction;

/**
 * Бот говорит в чат
 *
 * @author zella.
 */
public class TellBotAction extends AbstractBotAction {

    private final String whatToTell;

    public TellBotAction(ActorRef bot, String botId, String whatToTell) {
        super(bot, botId);
        this.whatToTell = whatToTell;
    }


    public String getWhatToTell() {
        return whatToTell;
    }

    public String getWhatToTellAsJson() {
        final JsonObject object = new JsonObject();
        object.addProperty("type", "replay");
        object.addProperty("data", whatToTell);
        return object.toString();
    }


    @Override
    public void execute() {
        bot.tell(this, ActorRef.noSender());
    }

    @Override
    public String toString() {
        return whatToTell;
    }
}
