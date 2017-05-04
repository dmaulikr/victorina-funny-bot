package org.zella.db;

import org.zella.config.ICredentials;

/**
 * Доступ к фразам из базы
 *
 * @author zella.
 */
public interface IBotDao {

    BotInfo findBy(ICredentials credentials);

    class BotInfo {

        public final ICredentials credentials;
        public final String botId;

        public BotInfo(ICredentials credentials, String botId) {
            this.credentials = credentials;
            this.botId = botId;
        }

        @Override
        public String toString() {
            return "BotInfo{" + credentials +
              ", " + botId + '\'' +
              '}';
        }
    }

}
