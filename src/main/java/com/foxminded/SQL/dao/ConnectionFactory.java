package com.foxminded.SQL.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    private final String driver;
    private final String url;
    private final String userName;
    private final String password;

    public ConnectionFactory(String driver, String url, String userName, String password) {
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public Connection connect() {
        Connection conn = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
