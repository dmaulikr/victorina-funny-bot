package org.zella.config;

import org.zella.utils.Utils;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author zella.
 */
public interface IConfig {

    /**
     * Шанс для какого-либо действия
     */
    float getChanceFor(String action);

    /**
     * Задерка для какого-либо действия
     */
    Range getDelayFor(String action);

    /**
     * прочий конфиг
     */
    Number getValue(String action);

    /**
     * @return bots mapped by id from db
     */
    Map<String, ICredentials> getAvailableBots();

    Optional<String> selectRandomBotExcept(Set<String> except);

    String getDbUrl();

    String getDbUser();

    String getDbPass();

    /**
     * @return sending ping period in millis
     */
    long getPingPeriod();

    String getServerAddress();

    String getServerHttpAddress();

    class Range {
        public final int min;
        public final int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int rand() {
            return Utils.randInRange(this);
        }
    }
}
