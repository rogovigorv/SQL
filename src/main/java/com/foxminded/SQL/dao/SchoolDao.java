package com.foxminded.SQL.dao;

import java.sql.SQLException;
import java.util.List;

public interface SchoolDao<T> {
    void insertToDB(List<T> e) throws SQLException;
}
