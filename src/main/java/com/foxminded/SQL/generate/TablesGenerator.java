package com.foxminded.SQL.generate;

import com.foxminded.SQL.dao.ConnectionFactory;
import org.hsqldb.cmdline.SqlFile;
import java.io.File;
import java.sql.Connection;

public class TablesGenerator {
    private static final String CREATE_SCRIPT =
            "C:\\Users\\User\\Desktop\\Foxminded\\SQL\\src\\main\\resources\\create tables.sql";

    public void create() {
        Connection conn = ConnectionFactory.connect();

        try {
            SqlFile sf = new SqlFile(new File(CREATE_SCRIPT));
            sf.setConnection(conn);
            sf.execute();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
        try {
            if (conn != null) {
                conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }
}
