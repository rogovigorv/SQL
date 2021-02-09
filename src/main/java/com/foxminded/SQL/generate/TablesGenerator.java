package com.foxminded.SQL.generate;

import com.foxminded.SQL.dao.ConnectionFactory;
import org.hsqldb.cmdline.SqlFile;
import java.io.File;
import java.sql.Connection;

public class TablesGenerator {
    private final String scriptFile;

    public TablesGenerator(String scriptFile) {
        this.scriptFile = scriptFile;
    }

    public void create(ConnectionFactory connectionFactory) {
        Connection conn = connectionFactory.connect();

        try {
            SqlFile sf = new SqlFile(new File(scriptFile));
            sf.setConnection(conn);
            sf.execute();

        } catch (Exception e) {
            e.printStackTrace();

        } try {
            if (conn != null) {
                conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
