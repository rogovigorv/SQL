package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Course;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CourseDao {
    private static final String CREATE_COURSES_SQL =
            "INSERT INTO courses (course_id, course_name, course_description) VALUES ";
    private static final String SELECT_ALL_COURSES_SQL = "SELECT * FROM courses ";
    private static final String SINGLE_QUOTE = "'";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String COMMA = ",";
    private static final String TAB = " ";

    public void insertToDB(List<Course> courses) throws SQLException {
        try (Connection conn = ConnectionFactory.connect();
             Statement stat = conn.createStatement()) {

            courses.forEach(c -> {
                try {
                    stat.executeUpdate(
                            CREATE_COURSES_SQL +
                                    LEFT_PARENTHESIS +
                                    SINGLE_QUOTE +
                                    c.getCourseID() +
                                    SINGLE_QUOTE +
                                    COMMA +
                                    TAB +
                                    SINGLE_QUOTE +
                                    c.getCourseName() +
                                    SINGLE_QUOTE +
                                    COMMA +
                                    TAB +
                                    SINGLE_QUOTE +
                                    c.getCourseDescription() +
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

    public Integer getCourseIDByName(String courseName) throws SQLException {
        int courseID = 0;

        try (Connection conn = ConnectionFactory.connect();
             Statement stat = conn.createStatement()) {

            ResultSet rs = stat.executeQuery(SELECT_ALL_COURSES_SQL);

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
