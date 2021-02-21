package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import static com.foxminded.SQL.dao.Queries.CREATE_GROUPS_SQL;

public class GroupDao implements SchoolDao<Group>{
    private final ConnectionFactory connectionFactory;

    public GroupDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void insertToDB(List<Group> groups) throws ExceptionDao {

        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(CREATE_GROUPS_SQL)) {

            groups.forEach(g -> {
                try {
                    stat.setInt(1, g.getGroupID());
                    stat.setString(2, g.getGroupName());
                    stat.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throw new ExceptionDao("Groups table is not filled." +
                    " Method insertToDB in GroupDao class collapsed.");
        }
    }
}
