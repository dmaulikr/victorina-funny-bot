package org.zella.db;

import com.orientechnologies.orient.core.db.OPartitionedDatabasePoolFactory;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.zella.config.impl.Config;

/**
 * @author zella.
 */
public class DB {


    //TODO it's better to use di

    protected final static String sDbUrl = Config.getInstance().getDbUrl();
    protected final static String sDbUser = Config.getInstance().getDbUser();
    protected final static String sDbPassword = Config.getInstance().getDbPass();

    private static OPartitionedDatabasePoolFactory poolFactory = new OPartitionedDatabasePoolFactory();


    public static ODatabaseDocumentTx acquire() {
        return poolFactory.get(sDbUrl, sDbUser, sDbPassword).acquire();
    }


}