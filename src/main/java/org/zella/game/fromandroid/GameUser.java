package org.zella.game.fromandroid;


import com.google.gson.JsonObject;
import org.zella.config.IConfig;
import org.zella.config.impl.Config;

/**
 * Created by dru on 11.01.16.
 */
public class GameUser {

    private static IConfig config = Config.getInstance();

    private final String id;

    private final String name;

    private final String avatar;

    private final int score;

    private final int rating;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        // avatar can be link(vk, fb ) or relative path (if store on our server)
        String tmpAvatar = avatar;
        if (!tmpAvatar.startsWith("http")) {
            tmpAvatar = config.getServerHttpAddress() + "avatar?file=" + avatar;
        }

        return tmpAvatar;
    }

    public int getScore() {
        return score;
    }

    public int getRating() {
        return rating;
    }


    private GameUser(String id, String name, String avatar, int score, int rating) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.score = score;
        this.rating = rating;
    }

    public static GameUser fromJson(JsonObject json) {
        return new GameUser(
          json.get("playerId").getAsString(),
          json.get("name").getAsString(),
          json.get("avatar").getAsString(),
          json.get("score").getAsInt(),
          json.get("rating").getAsInt()
        );
    }

    @Override
    public String toString() {
        return "GameUser{" +
          "id='" + id + '\'' +
          ", name='" + name + '\'' +
          ", avatar='" + avatar + '\'' +
          ", score=" + score +
          ", rating=" + rating +
          '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameUser user = (GameUser) o;

        if (score != user.score) return false;
        if (rating != user.rating) return false;
        if (!id.equals(user.id)) return false;
        if (!name.equals(user.name)) return false;
        return !(avatar != null ? !avatar.equals(user.avatar) : user.avatar != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + score;
        result = 31 * result + rating;
        return result;
    }
}
