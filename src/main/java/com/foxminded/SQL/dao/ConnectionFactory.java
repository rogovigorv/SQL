package com.foxminded.SQL.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USERNAME = "jhon_doe";
    private static final String PASSWORD = "829893";

    public static Connection connect() {
        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(POSTGRES_URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
