package org.zella.game.fromandroid;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * Created by dru on 11.01.16.
 */
public class GameState {

    private static final int sMaxChatLines = 30;

    private String question;

    private String hint;

    private long timeUntilQuestion;

    private long timeUntilHint;

    private List<ChatMessage> chat = new ArrayList<>();

    /**
     * key - player id
     */
    private final Map<String, GameUser> players;


    public String getQuestion() {
        return question;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setTimeUntilHint(long timeUntilHint) {
        this.timeUntilHint = timeUntilHint;
        this.timeUntilQuestion = -1;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setTimeUntilQuestion(long timeUntilQuestion) {
        this.timeUntilQuestion = timeUntilQuestion;
        this.timeUntilHint = -1;
    }

    public List<ChatMessage> getChat() {
        return chat;
    }

    public void setChat(List<ChatMessage> chat) {
        this.chat = chat;
    }

    public void addChatMessage(ChatMessage message) {
        if (chat.size() >= sMaxChatLines) {
            //remove first
            chat.remove(0);
        }
        chat.add(message);
    }

    /**
     * @return -1 если не используется
     */
    public long getTimeUntilQuestion() {
        return timeUntilQuestion;
    }

    /**
     * @return -1 если не используется
     */
    public long getTimeUntilHint() {
        return timeUntilHint;
    }

    public GameUser getPlayer(String playerId) {
        return players.get(playerId);
    }

    /**
     * @return players sorted by scores
     */
    public List<GameUser> getPlayers() {
        List<GameUser> users = new ArrayList<>(players.values());
        Collections.sort(users, new Comparator<GameUser>() {
            @Override
            public int compare(GameUser lhs, GameUser rhs) {
                return rhs.getScore() - lhs.getScore();
            }
        });
        return users;
    }

    private GameState(String question, String hint, long timeUntilQuestion, long timeUntilHint, Map<String, GameUser> players) {
        this.question = question;
        this.hint = hint;
        this.timeUntilQuestion = timeUntilQuestion;
        this.timeUntilHint = timeUntilHint;
        this.players = players;
    }

    public static GameState fromJson(JsonObject json) {

        final JsonArray playersJson = json.get("player_states").getAsJsonArray();

        final Map<String, GameUser> playerStateMap = new HashMap<>();

        for (JsonElement jsonElement : playersJson) {
            GameUser u = GameUser.fromJson(jsonElement.getAsJsonObject());
            playerStateMap.put(u.getId(), u);
        }

        return new GameState(
          json.get("question").getAsString(),
          json.get("hint").getAsString(),
          json.has("timeUntilQuestion") ? json.get("timeUntilQuestion").getAsLong() : -1,
          json.has("timeUntilHint") ? json.get("timeUntilHint").getAsLong() : -1,
          playerStateMap
        );
    }

    public static Optional<GameUser> retrieveJoinedUser(GameState oldGameState, GameState newGameState) {
        return newGameState.getPlayers().stream()
          .filter(gameUser -> !oldGameState.players.containsKey(gameUser.getId()))
          .findFirst();
    }

    public static Optional<GameUser> retrieveLeavedUser(GameState oldGameState, GameState newGameState) {
        return oldGameState.getPlayers().stream()
          .filter(gameUser -> !newGameState.players.containsKey(gameUser.getId()))
          .findFirst();
    }

    @Override
    public String toString() {
        return "GameState{" +
          "question='" + question + '\'' +
          ", hint='" + hint + '\'' +
          ", timeUntilQuestion=" + timeUntilQuestion +
          ", timeUntilHint=" + timeUntilHint +
          ", players=" + players +
          '}';
    }
}
