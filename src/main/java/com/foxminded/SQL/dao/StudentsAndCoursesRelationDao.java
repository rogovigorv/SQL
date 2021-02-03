package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Student;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentsAndCoursesRelationDao {
    private static final String CREATE_STUDENTS_AND_COURSES_RELATION_SQL =
            "INSERT INTO studentsAndCoursesRelation (student_id, course_id) VALUES ";
    private static final String SELECT_ALL_STUDENTS_AND_COURSES_RELATION_SQL =
            "SELECT * FROM studentsAndCoursesRelation ";
    private static final String SINGLE_QUOTE = "'";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String COMMA = ",";
    private static final String TAB = " ";

    public void insertToDB(List<Student> students) throws SQLException {
        try (Connection conn = ConnectionFactory.connect();
             Statement stat = conn.createStatement()) {

                try {
                    for (Student student : students) {
                        for (Map.Entry<Integer, Integer> entry : placeInTheTable(student).entrySet()) {
                            int courseID = entry.getKey();
                            int studentID = entry.getValue();
                            stat.executeUpdate(CREATE_STUDENTS_AND_COURSES_RELATION_SQL +
                                    LEFT_PARENTHESIS +
                                    SINGLE_QUOTE +
                                    studentID +
                                    SINGLE_QUOTE +
                                    COMMA +
                                    TAB +
                                    SINGLE_QUOTE +
                                    courseID +
                                    SINGLE_QUOTE +
                                    RIGHT_PARENTHESIS);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        } catch (SQLException throwables) {
            throw new SQLException();
        }
    }

    public Set<Integer> getStudentsIDByCourseID(int courseID) throws SQLException {
        Set<Integer> studentsID = new HashSet<>();

        try (Connection conn = ConnectionFactory.connect();
             Statement stat = conn.createStatement()) {

            ResultSet rs = stat.executeQuery(SELECT_ALL_STUDENTS_AND_COURSES_RELATION_SQL);

            while (rs.next()) {
                if (courseID == (rs.getInt(2))) {
                    studentsID.add(rs.getInt(1));
                }
            }

        } catch (SQLException throwables) {
            throw new SQLException();
        }

        return studentsID;
    }

    public void addToTheCourse(List<Student> students,  List<Course> courses) throws SQLException {
        try (Connection conn = ConnectionFactory.connect();
             Statement stat = conn.createStatement()) {

            List<Map<Integer, Integer>> fromDataBase = new ArrayList<>();

            ResultSet rs = stat.executeQuery(SELECT_ALL_STUDENTS_AND_COURSES_RELATION_SQL);

            while (rs.next()) {
                fromDataBase.add(getFromDB(rs.getInt(2), rs.getInt(1)));
            }

            for (Course course : courses) {
                List<Student> studentEnrolToTheCourse = new ArrayList<>();

                fromDataBase.forEach(f -> f.forEach((key, value) -> {

                    if(f.get(course.getCourseID()) != null) {
                        int studentID = f.get(course.getCourseID());
                        students.forEach(s -> {

                            if (s.getStudentID() == studentID) {
                                studentEnrolToTheCourse.add(s);
                            }
                        });
                    }
                }));

               // studentEnrolToTheCourse.forEach(s -> System.out.println(s.toString() + " " + "added to " + course.getCourseName()));
            }

        } catch (SQLException throwables) {
            throw new SQLException();
        }
    }

    private Map<Integer, Integer> placeInTheTable(Student student) {
        Map<Integer, Integer> result = new HashMap<>();

        for (int i = 0; i < student.getCourses().size(); i++) {
            int courseID = student.getCourses().get(i).getCourseID();
            result.put(courseID, student.getStudentID());
        }

        return result;
    }

    private Map<Integer, Integer> getFromDB(int courseID, int studentID) {
        Map<Integer, Integer> result = new HashMap<>();
        result.put(courseID, studentID);
            return result;
        }

}
