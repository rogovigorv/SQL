package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    private static final String CREATE_STUDENTS_SQL =
            "INSERT INTO students (student_id, group_id, first_name, last_name) VALUES ";
    private static final String SELECT_ALL_STUDENTS_SQL = "SELECT * FROM students";
    private static final String DELETE_STUDENT_BY_ID = "DELETE FROM students WHERE student_id = ?";
    private static final String SINGLE_QUOTE = "'";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String COMMA = ",";
    private static final String TAB = " ";

    public void addAllStudents(List<Student> students) throws SQLException {
        try (Connection conn = ConnectionFactory.connect();
             Statement stat = conn.createStatement()) {

            students.forEach(s -> {
                try {
                    stat.executeUpdate(
                            CREATE_STUDENTS_SQL +
                                    LEFT_PARENTHESIS +
                                    SINGLE_QUOTE +
                                    s.getStudentID() +
                                    SINGLE_QUOTE +
                                    COMMA +
                                    TAB +
                                    SINGLE_QUOTE +
                                    s.getGroup().getGroupID() +
                                    SINGLE_QUOTE +
                                    COMMA +
                                    TAB +
                                    SINGLE_QUOTE +
                                    s.getFirstName() +
                                    SINGLE_QUOTE +
                                    COMMA +
                                    TAB +
                                    SINGLE_QUOTE +
                                    s.getLastName() +
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

    public List<Integer> getStudentByGroupID() throws SQLException {
        List<Integer> groupsID = new ArrayList<>();

        try (Connection conn = ConnectionFactory.connect();
             Statement stat = conn.createStatement()) {

            ResultSet rs = stat.executeQuery(SELECT_ALL_STUDENTS_SQL);

            while (rs.next()) {
                int groupID = rs.getInt(2);
                groupsID.add(groupID);
            }

        } catch (SQLException throwables) {
            throw new SQLException();
        }

        return groupsID;
    }

    public void addNewStudent(Student student) throws SQLException {
        try (Connection conn = ConnectionFactory.connect();
             Statement stat = conn.createStatement()) {

            stat.executeUpdate(
                    CREATE_STUDENTS_SQL +
                            LEFT_PARENTHESIS +
                            SINGLE_QUOTE +
                            student.getStudentID() +
                            SINGLE_QUOTE +
                            COMMA +
                            TAB +
                            SINGLE_QUOTE +
                            student.getGroup().getGroupID() +
                            SINGLE_QUOTE +
                            COMMA +
                            TAB +
                            SINGLE_QUOTE +
                            student.getFirstName() +
                            SINGLE_QUOTE +
                            COMMA +
                            TAB +
                            SINGLE_QUOTE +
                            student.getLastName() +
                            SINGLE_QUOTE +
                            RIGHT_PARENTHESIS);
        } catch (SQLException throwables) {
            throw new SQLException();
        }
    }

    public void deleteStudentByID(int studentID) throws SQLException {

        try (Connection conn = ConnectionFactory.connect();
             PreparedStatement prepareStatement = conn.prepareStatement(DELETE_STUDENT_BY_ID)) {

            prepareStatement.setInt(1, studentID);
            prepareStatement.executeUpdate();

        } catch (SQLException throwables) {
            throw new SQLException();
        }
    }
}
