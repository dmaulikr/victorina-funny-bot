package org.zella.game.fromandroid;

/**
 * Created by dru on 27.01.16.
 */
public class ChatMessage {

    private final Type type;

    private final CharSequence text;

    private final String url;

    private ChatMessage(Type type, String text, String url) {
        this.type = type;
        this.text = text;
        this.url = url;
    }

    public static ChatMessage fromChatReplay(String name, String whatSay, String url) {
        return new ChatMessage(Type.REPLAY, name + ": " + whatSay, url);
    }

    public static ChatMessage fromCorrectAnswer(String name, String answer,String url) {
        return new ChatMessage(Type.ANSWER_CORRECT, name + " ответил верно: \"" + answer + "\"", url);
    }

    public static ChatMessage fromNobodyAnswer(String answer,String url) {
        return new ChatMessage(Type.NOBODY_ANSWER, "Никто не ответил, правильный ответ: \"" + answer + "\"", url);
    }

    public Type getType() {
        return type;
    }

    public CharSequence getText() {
        return text;
    }

    @Override
    public String toString() {
        return text.toString();
    }

    public String getUrl() {
        return url;
    }

    enum Type {NOBODY_ANSWER, ANSWER_CORRECT, REPLAY}
}
