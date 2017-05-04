package org.zella.net.fromandroid.model;



import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.zella.config.IConfig;
import org.zella.config.impl.Config;
import org.zella.utils.Constants;

/**
 * Created by dru on 29.01.16.
 */
public class User  {

    private static IConfig config = Config.getInstance();

    private static final Gson gson = new Gson();
    private static final JsonParser parser = new JsonParser();

    private String userId;
    private String name;
    private String avatar;
    private int rating;


    public String getPlayerId() {
        return userId;
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

    public int getRating() {
        return rating;
    }

    public static User fromJson(JsonElement element) {
        return gson.fromJson(element, User.class);
    }

    public static User fromJson(String jsonString) {
        return gson.fromJson(parser.parse(jsonString), User.class);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", rating=" + rating +
                '}';
    }



    public User() {
    }


}
