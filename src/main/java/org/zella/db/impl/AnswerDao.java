package org.zella.db.impl;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.slf4j.LoggerFactory;
import org.zella.db.DB;
import org.zella.db.IAnswersDao;

import java.util.List;

/**
 * @author zella.
 */
public class AnswerDao implements IAnswersDao {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AnswerDao.class);


    @Override
    public String findAnswer(String question) {
        try (ODatabaseDocumentTx db = DB.acquire()) {
            List<ODocument> result = db.command(new OSQLSynchQuery<>("select answer from Question where question = ?"))
              .execute(
                question);
            assert result.size() == 1;
            log.debug("Answer for \"{}\" is \"{}\"", question, result.get(0).field("answer"));
            return result.get(0).field("answer");
        }
    }
}
