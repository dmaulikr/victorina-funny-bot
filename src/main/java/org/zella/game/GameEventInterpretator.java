package org.zella.game;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.LoggerFactory;
import org.zella.events.IGameEvent;
import org.zella.events.impl.*;
import org.zella.game.fromandroid.ChatMessage;
import org.zella.game.fromandroid.GameState;
import org.zella.game.fromandroid.GameUser;
import org.zella.game.fromandroid.Winner;

import java.util.List;
import java.util.Optional;

/**
 * Тут бот определяет что ему пришло с сервака и запоминает текущее игровое событие
 * <p>
 * Тут вся логика создания игровых событий по сообщению с сервака, короче мозги бота
 *
 * @author zella.
 */
public class GameEventInterpretator {


    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GameEventInterpretator.class);


    private GameState gameState = null;

    public IGameEvent createFromServerMessage(String message) {
        JsonObject json = new JsonParser().parse(message).getAsJsonObject();

        String type = json.get("type").getAsString();

        switch (type) {
            case "game_info":

                gameState = GameState.fromJson(json.get("game_state").getAsJsonObject());

                return new GetAllInfoWhenJoinGameEvent(gameState);
            case "hint":
                String hint = json.get("hint").getAsString();
                long timeUntilHint = json.get("timeUntilHint").getAsLong();

                gameState.setHint(hint);
                gameState.setTimeUntilHint(timeUntilHint);

                log.debug("step 1 receive hint");
                return new HintGameEvent(gameState, hint);

            case "nobody_answered":

                final List<ChatMessage> chat___ = gameState.getChat();

                gameState = GameState.fromJson(json.get("game_state").getAsJsonObject());

                chat___.add(ChatMessage.fromNobodyAnswer(gameState.getHint(), null));
                gameState.setChat(chat___);

                return new NobodyAnsweredGameEvent(gameState, gameState.getHint());

            case "answered_correct":
                final List<ChatMessage> chat__ = gameState.getChat();

                gameState = GameState.fromJson(json.get("game_state").getAsJsonObject());

                Winner winner = Winner.fromJson(json.get("winner").getAsJsonObject());

                chat__.add(ChatMessage.fromCorrectAnswer(
                  gameState.getPlayer(winner.getPlayerId()).getName(),
                  gameState.getHint(), gameState.getPlayer(winner.getPlayerId()).getAvatar()
                ));
                gameState.setChat(chat__);

                boolean isGameEnded = json.get("gameEnded").getAsBoolean();

                return new AnsweredCorrectGameEvent(gameState, gameState.getHint(), winner, isGameEnded);

            case "player_joined":

                final List<ChatMessage> chat = gameState.getChat();


                GameState newGameState = GameState.fromJson(json.get("game_state").getAsJsonObject());

                log.trace("players in state {}", gameState.getPlayers().size());
                log.trace("players in newState {}", newGameState.getPlayers().size());
                Optional<GameUser> joinedPlayer = GameState.retrieveJoinedUser(gameState, newGameState);
                gameState = newGameState;
                gameState.setChat(chat);


                if (joinedPlayer.isPresent()) {
                    return new PlayerJoinedGameEvent(gameState, joinedPlayer.get());
                } else break;

            case "player_left":

                final List<ChatMessage> chat_ = gameState.getChat();


                GameState newGameState_ = GameState.fromJson(json.get("game_state").getAsJsonObject());
                Optional<GameUser> leavedPlayer = GameState.retrieveLeavedUser(gameState, newGameState_);
                gameState = newGameState_;
                gameState.setChat(chat_);

                if (leavedPlayer.isPresent()) {
                    return new PlayerLeavedGameEvent(gameState, leavedPlayer.get());
                } else break;

            case "chat_replay":

                String playerId = json.get("playerId").getAsString();
                String text = json.get("text").getAsString();

                gameState.addChatMessage(
                  ChatMessage.fromChatReplay(gameState.getPlayer(playerId).getName(), text, gameState.getPlayer(playerId).getAvatar())
                );

                return new MessageInChatGameEvent(gameState, playerId, text);
            case "question":

                String question_ = json.get("question").getAsString();
                String hint_ = json.get("hint").getAsString();
                long timeUntilHint_ = json.get("timeUntilHint").getAsLong();

                gameState.setQuestion(question_);
                gameState.setHint(hint_);
                gameState.setTimeUntilHint(timeUntilHint_);

                return new NewQuestionGameEvent(gameState, gameState.getQuestion());
        }

        throw new RuntimeException(type + " event not proceed, implement it!");
//        return null;
    }
}
