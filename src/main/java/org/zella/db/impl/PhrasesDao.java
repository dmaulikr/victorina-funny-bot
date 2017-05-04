package org.zella.db.impl;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.slf4j.LoggerFactory;
import org.zella.db.DB;
import org.zella.db.IPhrasesDao;
import org.zella.utils.Utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zella.
 */
public class PhrasesDao implements IPhrasesDao {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PhrasesDao.class);


    @Override
    public Collection<String> findPhrasesByType(String type) {
        try (ODatabaseDocumentTx db = DB.acquire()) {
            List<ODocument> result = db.command(new OSQLSynchQuery<>("select text from Phrases where type = ? "))
              .execute(
                type);

            if ( result.size() > 0) throw new IllegalStateException("phrases not found for type: "+type);

            return result.stream().map(entries -> entries.<String>field("text")).collect(Collectors.toList());
        }
    }

    private long phrasesCount(ODatabaseDocumentTx db, String type) {
        List<ODocument> list = db.query(new OSQLSynchQuery<ODocument>("SELECT COUNT(*) as count FROM Phrases WHERE type = ?"), type);
        return list.get(0).field("count");
    }

    @Override
    public String randomPhraseByType(String type) {
        try (ODatabaseDocumentTx db = DB.acquire()) {
            List<ODocument> result = db.command(new OSQLSynchQuery<>("select text from Phrases where type = ? SKIP ? LIMIT 1"))
              .execute(
                type,
                Utils.randInt(0, (int) (phrasesCount(db, type) - 1)));
            assert result.size() == 1;
            log.trace("Phrase for \"{}\" is \"{}\"", type, result.get(0).field("text"));
            return result.get(0).field("text");
        }
    }
}
