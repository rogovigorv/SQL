package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Group;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class GroupDao {
    private static final String CREATE_GROUPS_SQL = "INSERT INTO groups (group_id, group_name) VALUES ";
    private static final String SINGLE_QUOTE = "'";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String COMMA = ",";
    private static final String TAB = " ";

    public void insertToDB(List<Group> groups) throws SQLException {

        try (Connection conn = ConnectionFactory.connect();
             Statement stat = conn.createStatement()) {

            groups.forEach(g -> {
                try {
                    stat.executeUpdate(
                            CREATE_GROUPS_SQL +
                                    LEFT_PARENTHESIS +
                                    SINGLE_QUOTE +
                                    g.getGroupID() +
                                    SINGLE_QUOTE +
                                    COMMA +
                                    TAB +
                                    SINGLE_QUOTE +
                                    g.getGroupName() +
                                    SINGLE_QUOTE +
                                    RIGHT_PARENTHESIS);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throw new SQLException();
        }
    }
}
