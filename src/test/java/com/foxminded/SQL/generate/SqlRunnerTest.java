package com.foxminded.SQL.generate;

import com.foxminded.SQL.dao.ConnectionFactory;
import com.foxminded.SQL.dao.CourseDao;
import com.foxminded.SQL.dao.ExceptionDao;
import com.foxminded.SQL.dao.GroupDao;
import com.foxminded.SQL.dao.StudentDao;
import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Group;
import com.foxminded.SQL.domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static com.foxminded.SQL.dao.Queries.SELECT_ALL_COURSES_SQL;
import static com.foxminded.SQL.dao.Queries.SELECT_ALL_GROUPS_SQL;
import static com.foxminded.SQL.dao.Queries.SELECT_ALL_STUDENTS_AND_COURSES_RELATION_SQL;
import static com.foxminded.SQL.dao.Queries.SELECT_ALL_STUDENTS_SQL;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlRunnerTest {
    private static final String DRIVER = "org.h2.Driver";
    private static final String H2_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static final String SCRIPT_FILE = "src\\main\\resources\\create_tables.sql";
    private static final String COURSES_LIST_FILE = "src\\main\\resources\\courses.txt";
    private static final String STUDENT_FIRST_NAMES_LIST_FILE = "src\\main\\resources\\student_first_names.txt";
    private static final String STUDENT_LAST_NAMES_LIST_FILE = "src\\main\\resources\\student_last_names.txt";
    private static final String TAB = " ";
    private static final String LINE_BREAK = "\n";

    private static final List<Student> STUDENT = new ArrayList<>();
    private static final List<Group> GROUP = new LinkedList<>();
    private static final List<Course> THREE_TEST_COURSES = new LinkedList<>();
    private static List<Course> courses = new LinkedList<>();

    private static SqlRunner tables;
    private static DataGenerator data;
    private static ConnectionFactory connectionFactory;
    private static CourseDao courseDao;
    private static GroupDao groupDao;
    private static StudentDao studentDao;

    @BeforeAll
    static void setup() {
        tables = new SqlRunner(SCRIPT_FILE);
        data = new DataGenerator(COURSES_LIST_FILE, STUDENT_FIRST_NAMES_LIST_FILE, STUDENT_LAST_NAMES_LIST_FILE);
        connectionFactory = new ConnectionFactory(DRIVER, H2_URL, USERNAME, PASSWORD);
        courseDao = new CourseDao(connectionFactory);
        groupDao = new GroupDao(connectionFactory);
        studentDao = new StudentDao(connectionFactory);

        createTestData();
        insertTestDataToDB();
    }

    @Test
    void shouldCompareDataFromTheDatabaseFromTheGroupTableWithExpectedGroup() {
        String expected = GROUP.get(0).toString();

        StringBuilder fromDB = new StringBuilder();

        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(SELECT_ALL_GROUPS_SQL)) {

            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                int groupID = rs.getInt(1);
                String groupName = rs.getString(2);

                fromDB.append(groupID)
                        .append(TAB)
                        .append(groupName);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String actual = fromDB.toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldCompareDataFromTheDatabaseFromTheCourseTableWithCourseList() {
        StringBuilder fromCourseList = new StringBuilder();
        courses.forEach(c -> fromCourseList.append(c.toString())
                .append(LINE_BREAK));

        String expected = fromCourseList.toString();

        StringBuilder fromDB = new StringBuilder();

        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(SELECT_ALL_COURSES_SQL)) {

            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                int courseID = rs.getInt(1);
                String courseName = rs.getString(2);
                String courseDescription = rs.getString(3);

                fromDB.append(courseID)
                        .append(TAB)
                        .append(courseName)
                        .append(TAB)
                        .append(courseDescription)
                        .append(LINE_BREAK);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String actual = fromDB.toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldCompareDataFromTheDatabaseFromTheStudentsTableWithStudentsList() {
        String expected = STUDENT.get(0).toString();

        StringBuilder fromDB = new StringBuilder();

        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat = conn.prepareStatement(SELECT_ALL_STUDENTS_SQL)) {

            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                int studentID = rs.getInt(1);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);

                fromDB.append(studentID)
                        .append(TAB)
                        .append(firstName)
                        .append(TAB)
                        .append(lastName);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String actual = fromDB.toString();

        assertEquals(expected, actual);
    }

    @Test
    void shouldCompareDataFromTheDatabaseFromTheStudentsAndCoursesRelationTableWithStudentsList() {
        String expected = 1 + TAB + 1 + TAB + 1 + TAB + 2 + TAB + 1 + TAB + 3 + TAB;

        StringBuilder fromDB = new StringBuilder();

        try (Connection conn = connectionFactory.connect();
             PreparedStatement stat =
                     conn.prepareStatement(SELECT_ALL_STUDENTS_AND_COURSES_RELATION_SQL)) {

            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                int studentID = rs.getInt(1);
                int courseID = rs.getInt(2);

                fromDB.append(studentID)
                        .append(TAB)
                        .append(courseID)
                        .append(TAB);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String actual = fromDB.toString();

        assertEquals(expected, actual);
    }

    private static void createTestData() {
        tables.runScript(connectionFactory);

        GROUP.add(new Group(1, "LR-19"));
        courses = data.generateCourses();

        THREE_TEST_COURSES.add(courses.get(0));
        THREE_TEST_COURSES.add(courses.get(1));
        THREE_TEST_COURSES.add(courses.get(2));

        STUDENT.add(new Student(1, GROUP.get(0), "James", "Hetfield", THREE_TEST_COURSES));
    }

    private static void insertTestDataToDB() throws ExceptionDao {
            groupDao.insertToDB(GROUP);
            courseDao.insertToDB(courses);
            studentDao.insertToDB(STUDENT);
            studentDao.assignAllStudentsToCourses(STUDENT);
    }
}
