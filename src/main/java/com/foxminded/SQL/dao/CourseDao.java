package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import static com.foxminded.SQL.dao.Queries.CREATE_COURSES_SQL;
import static com.foxminded.SQL.dao.Queries.SELECT_ALL_COURSES_SQL;

public class CourseDao implements SchoolDao<Course> {
    private final ConnectionFactory connectionFactory;

    public CourseDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void insertToDB(List<Course> courses) throws SQLException {
        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(CREATE_COURSES_SQL)) {

            courses.forEach(c -> {
                try {
                    stat.setInt(1, c.getCourseID());
                    stat.setString(2, c.getCourseName());
                    stat.setString(3, c.getCourseDescription());
                    stat.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throw new SQLException();
        }
    }

    public Integer getCourseIDByName(String courseName) throws SQLException {
        int courseID = 0;

        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(SELECT_ALL_COURSES_SQL)) {

            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                if (courseName.equals(rs.getString(2))) {
                    courseID = rs.getInt(1);
                }
            }

        } catch (SQLException throwables) {
            throw new SQLException();
        }

        return courseID;
    }
}
