package org.zella.config.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import org.slf4j.LoggerFactory;
import org.zella.config.IConfig;
import org.zella.config.ICredentials;
import org.zella.utils.Utils;

import java.util.*;

/**
 * @author zella.
 */
public class Config implements IConfig {


    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Config.class);

    private static Config instance;

    private final Map<String, ICredentials> availableBotsMap = new HashMap<>();

    private final Map<String, Float> chances = new HashMap<>();

    private final Map<String, Range> delays = new HashMap<>();

    private final Map<String, Number> values = new HashMap<>();

    private final String dbUrl;
    private final String dbUser;
    private final String dbPass;

    private final long pingPeriod;
    private final String serverAddress;
    private final String serverHttpAddress;

    public Config() {
        com.typesafe.config.Config config = ConfigFactory.load();

        String availableBots = config.getList("bots.credentials").render(ConfigRenderOptions.concise());
        JsonArray jsonArray = (JsonArray) new JsonParser().parse(availableBots);
        for (JsonElement object : jsonArray) {
            JsonObject ob = (JsonObject) object;
            String id = ob.get("id").getAsString();
            String email = ob.get("email").getAsString();
            String pass = ob.get("pass").getAsString();
            ICredentials bc = new BotCredentials(email, pass);
            availableBotsMap.put(id, bc);
        }

        //chances
        String chancesString = config.getList("bots.chances").render(ConfigRenderOptions.concise());
        JsonArray chancesArray = (JsonArray) new JsonParser().parse(chancesString);
        for (JsonElement object : chancesArray) {
            JsonObject ob = (JsonObject) object;
            String type = ob.get("type").getAsString();
            float chance = ob.get("chance").getAsFloat();
            chances.put(type, chance);
        }

        //delays
        String delaysString = config.getList("bots.delays").render(ConfigRenderOptions.concise());
        JsonArray delaysArray = (JsonArray) new JsonParser().parse(delaysString);
        for (JsonElement object : delaysArray) {
            JsonObject ob = (JsonObject) object;
            String type = ob.get("type").getAsString();
            int min = ob.get("min").getAsInt();
            int max = ob.get("max").getAsInt();
            delays.put(type, new Range(min, max));
        }

        //values
        String valString = config.getList("bots.values").render(ConfigRenderOptions.concise());
        JsonArray valsArray = (JsonArray) new JsonParser().parse(valString);
        for (JsonElement object : valsArray) {
            JsonObject ob = (JsonObject) object;
            String type = ob.get("type").getAsString();
            Number val = ob.get("value").getAsNumber();
            values.put(type, val);
        }

        //db
        dbUrl = config.getString("orient.db_url");
        dbUser = config.getString("orient.db_user");
        dbPass = config.getString("orient.db_pass");

        // network
        pingPeriod = config.getLong("network.pingPeriod");
        serverAddress = config.getString("network.serverAddress");
        serverHttpAddress = config.getString("network.serverHttpAddress");
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    @Override
    public float getChanceFor(String action) {
        assert chances.get(action) != null;
        return chances.get(action);
    }

    @Override
    public Range getDelayFor(String action) {
        assert delays.get(action) != null;
        return delays.get(action);
    }

    @Override
    public Number getValue(String action) {
        assert values.get(action) != null;
        return values.get(action);
    }

    @Override
    public Map<String, ICredentials> getAvailableBots() {
        return availableBotsMap;
    }

    @Override
    public Optional<String> selectRandomBotExcept(Set<String> except) {

        log.debug("excepts:");
        except.forEach(n -> log.debug(n + " "));
        log.debug("excepts end");
        final Set<String> avilableBots = new HashSet<>(availableBotsMap.keySet());
        avilableBots.removeAll(except);
        if (avilableBots.isEmpty())
            return Optional.empty();
        return Optional.of(Utils.randomElement(avilableBots));
    }

    @Override
    public String getDbUrl() {
        return dbUrl;
    }

    @Override
    public String getDbUser() {
        return dbUser;
    }

    @Override
    public String getDbPass() {
        return dbPass;
    }

    @Override
    public long getPingPeriod() {
        return pingPeriod;
    }

    @Override
    public String getServerAddress() {
        return serverAddress;
    }

    @Override
    public String getServerHttpAddress() {
        return serverHttpAddress;
    }

}
