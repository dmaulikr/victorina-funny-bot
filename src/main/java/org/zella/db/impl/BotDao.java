package org.zella.db.impl;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.slf4j.LoggerFactory;
import org.zella.config.ICredentials;
import org.zella.db.DB;
import org.zella.db.IBotDao;

import java.util.List;

/**
 * @author zella.
 */
public class BotDao implements IBotDao {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(BotDao.class);

    @Override
    public BotInfo findBy(ICredentials credentials) {
        try (ODatabaseDocumentTx db = DB.acquire()) {
            List<ODocument> result = db.command(new OSQLSynchQuery<>(
              "select from User where email = ? and pass = ?"))
              .execute(credentials.getEmail(), credentials.getPass());
            if (result.size() != 1) {
                throw new RuntimeException("Bot not exist:" + credentials.toString());
            }
            log.debug("Found user: {}", result.get(0));

            String rid = result.get(0).getIdentity().toString();

            return new BotInfo(credentials, rid.replace("#", ""));
        }
    }
}
