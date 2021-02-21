package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static com.foxminded.SQL.dao.Queries.CREATE_STUDENTS_AND_COURSES_RELATION_SQL;
import static com.foxminded.SQL.dao.Queries.CREATE_STUDENTS_SQL;
import static com.foxminded.SQL.dao.Queries.DELETE_STUDENT_FROM_COURSE_SQL;
import static com.foxminded.SQL.dao.Queries.DELETE_STUDENT_FROM_STUDENTS_AND_COURSES_RELATION_BY_ID;
import static com.foxminded.SQL.dao.Queries.DELETE_STUDENT_FROM_STUDENTS_BY_ID;
import static com.foxminded.SQL.dao.Queries.SELECT_ALL_STUDENTS_AND_COURSES_RELATION_SQL;
import static com.foxminded.SQL.dao.Queries.SELECT_ALL_STUDENTS_SQL;

public class StudentDao implements SchoolDao<Student>{
     private final ConnectionFactory connectionFactory;

    public StudentDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void insertToDB(List<Student> students) throws ExceptionDao {
        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(CREATE_STUDENTS_SQL)) {

            students.forEach(s -> {
                try {
                    stat.setInt(1, s.getStudentID());
                    stat.setInt(2, s.getGroup().getGroupID());
                    stat.setString(3, s.getFirstName());
                    stat.setString(4, s.getLastName());
                    stat.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException throwables) {
            throw new ExceptionDao("Students table is not filled." +
                    " Method insertToDB in StudentDao class collapsed.");
        }
    }

    public List<Integer> getAllGroupIDs() throws ExceptionDao {
        List<Integer> groupsID = new ArrayList<>();

        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(SELECT_ALL_STUDENTS_SQL)) {

            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                int groupID = rs.getInt(2);
                groupsID.add(groupID);
            }

        } catch (SQLException throwables) {
            throw new ExceptionDao("Can't get all group ID's." +
                    " Method getAllGroupIDs in StudentDao class collapsed.");
        }

        return groupsID;
    }

    public void addNewStudent(Student student) throws ExceptionDao {
        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(CREATE_STUDENTS_SQL)) {

            stat.setInt(1, student.getStudentID());
            stat.setInt(2, student.getGroup().getGroupID());
            stat.setString(3, student.getFirstName());
            stat.setString(4, student.getLastName());
            stat.executeUpdate();

        } catch (SQLException throwables) {
            throw new ExceptionDao("Can't add a new student." +
                    " Method addNewStudent in StudentDao class collapsed.");
        }
    }

    public void deleteStudentByID(int studentID) throws ExceptionDao {

        try (Connection conn = connectionFactory.connect();
             PreparedStatement relationshipTableStat = conn.prepareStatement(
                     DELETE_STUDENT_FROM_STUDENTS_AND_COURSES_RELATION_BY_ID)) {

            relationshipTableStat.setInt(1, studentID);
            relationshipTableStat.executeUpdate();

            PreparedStatement studentsTableStat = conn.prepareStatement(
                    DELETE_STUDENT_FROM_STUDENTS_BY_ID);

            studentsTableStat.setInt(1, studentID);
            studentsTableStat.executeUpdate();

        } catch (SQLException throwables) {
            throw new ExceptionDao("Can't delete student by ID." +
                    " Method deleteStudentByID in StudentDao class collapsed.");
        }
    }

    public void assignAllStudentsToCourses(List<Student> students) throws ExceptionDao {
        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(CREATE_STUDENTS_AND_COURSES_RELATION_SQL)) {

                for (Student student : students) {
                    for (Map.Entry<Integer, Integer> entry : placeInTheTable(student).entrySet()) {
                        int courseID = entry.getKey();
                        int studentID = entry.getValue();

                        stat.setInt(1, studentID);
                        stat.setInt(2, courseID);
                        stat.executeUpdate();
                    }
                }
        } catch (SQLException throwables) {
            throw new ExceptionDao("Can't assign students to the courses." +
                    " Method assignAllStudentsToCourses in StudentDao class collapsed.");
        }
    }

    public Set<Integer> getStudentsIDByCourseID(int courseID) throws ExceptionDao {
        Set<Integer> studentsID = new HashSet<>();

        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(SELECT_ALL_STUDENTS_AND_COURSES_RELATION_SQL)) {

            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                if (courseID == (rs.getInt(2))) {
                    studentsID.add(rs.getInt(1));
                }
            }

        } catch (SQLException throwables) {
            throw new ExceptionDao("Can't get students ID's by course ID." +
                    " Method getStudentsIDByCourseID in StudentDao class collapsed.");
        }

        return studentsID;
    }

    public void addToTheCourse(int studentID,  int courseID,
                               List<Student> students,  List<Course> courses) throws ExceptionDao {
        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(SELECT_ALL_STUDENTS_AND_COURSES_RELATION_SQL)) {

            List<Map<Integer, Integer>> fromDataBase = new ArrayList<>();
            List<Student> enrolledStudents = new ArrayList<>();
            Set<Student> preparedStudent = new HashSet<>();
            Set<Course> preparedCourse = new HashSet<>();

            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                fromDataBase.add(getFromDB(rs.getInt(2), rs.getInt(1)));
            }

            fromDataBase.forEach(f -> f.forEach((key, value) -> {
                if(f.get(courseID) != null) {
                    int enrolledStudentID = f.get(courseID);
                    students.forEach(s -> {
                        if(s.getStudentID() == enrolledStudentID) {
                            enrolledStudents.add(s);
                        }
                    });
                }
            }));

            enrolledStudents.forEach(e -> {
                if(studentID != e.getStudentID()) {
                    students.forEach(s -> {
                        if(s.getStudentID() == studentID) {
                            preparedStudent.add(s);
                        }
                    });
                }
            });

            courses.forEach(c -> {
                if(c.getCourseID() == courseID) {
                    preparedCourse.add(c);
                }
            });

            Student desiredStudent = preparedStudent.iterator().next();
            Course desiredCourse = preparedCourse.iterator().next();

            desiredStudent.addCourse(desiredCourse);

            addNewStudentToTheCourse(desiredStudent, courseID);

        } catch (SQLException throwables) {
            throw new ExceptionDao("Can't add student to the course." +
                    " Method addToTheCourse in StudentDao class collapsed.");
        }
    }

    public void removeFromCourse(int studentID, int courseID, List<Student> students) throws ExceptionDao {
        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(DELETE_STUDENT_FROM_COURSE_SQL)) {

            stat.setInt(1, studentID);
            stat.setInt(2,  courseID);
            stat.executeUpdate();

            students.forEach(s -> {
                if(s.getStudentID() == studentID) {
                    s.getCourses().forEach(c -> {
                        if(c.getCourseID() == courseID) {
                            s.getCourses().remove(c);
                        }
                    });
                }
            });

        } catch (SQLException throwables) {
            throw new ExceptionDao("Can't remove student from course." +
                    " Method removeFromCourse in StudentDao class collapsed.");
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


    private void addNewStudentToTheCourse(Student student, int courseID) throws ExceptionDao {
        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(CREATE_STUDENTS_AND_COURSES_RELATION_SQL)) {

            try {

                int studentID = student.getStudentID();

                stat.setInt(1, studentID);
                stat.setInt(2, courseID);
                stat.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throw new ExceptionDao("Can't add student to the course." +
                    " Method addNewStudentToTheCourse in StudentDao class collapsed.");
        }
    }
}
