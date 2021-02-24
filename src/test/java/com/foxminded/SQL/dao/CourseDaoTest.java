package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Group;
import com.foxminded.SQL.domain.Student;
import com.foxminded.SQL.generate.DataGenerator;
import com.foxminded.SQL.generate.SqlRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseDaoTest {
    private static final String DRIVER = "org.h2.Driver";
    private static final String H2_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static final String SCRIPT_FILE = "src\\main\\resources\\create_tables.sql";
    private static final String COURSES_LIST_FILE = "src\\main\\resources\\courses.txt";
    private static final String STUDENT_FIRST_NAMES_LIST_FILE = "src\\main\\resources\\student_first_names.txt";
    private static final String STUDENT_LAST_NAMES_LIST_FILE = "src\\main\\resources\\student_last_names.txt";
    private static final String COURSE_NAME = "math";

    private static final List<Student> STUDENT = new ArrayList<>();
    private static final List<Group> GROUP = new LinkedList<>();
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
    void shouldGetCourseIDByName() {
        int expected = 1;
        int actual = 0;

        try {
            actual = courseDao.getCourseIDByName(COURSE_NAME);
        } catch (ExceptionDao throwables) {
            throwables.printStackTrace();
        }

        assertEquals(expected, actual);
    }

    private static void createTestData() {
        tables.runScript(connectionFactory);

        GROUP.add(new Group(1, "LR-19"));
        courses = data.generateCourses();

        List<Course> threeCourses = new LinkedList<>();
        threeCourses.add(courses.get(0));
        threeCourses.add(courses.get(1));
        threeCourses.add(courses.get(2));

        STUDENT.add(new Student(1, GROUP.get(0), "James", "Hetfield", threeCourses));
    }

    private static void insertTestDataToDB() throws ExceptionDao {
            groupDao.insertToDB(GROUP);
            courseDao.insertToDB(courses);
            studentDao.insertToDB(STUDENT);
            studentDao.assignAllStudentsToCourses(STUDENT);
    }
}
