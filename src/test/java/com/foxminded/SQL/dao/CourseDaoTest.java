package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Group;
import com.foxminded.SQL.domain.Student;
import com.foxminded.SQL.generate.DataGenerator;
import com.foxminded.SQL.generate.TablesGenerator;
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
    private static final String COURSE_NAME = "math";

    private static final List<Student> STUDENT = new ArrayList<>();
    private static final List<Group> GROUP = new LinkedList<>();
    private static List<Course> courses = new LinkedList<>();

    private static final TablesGenerator TABLES = new TablesGenerator(SCRIPT_FILE);
    private static final DataGenerator DATA = new DataGenerator(COURSES_LIST_FILE);
    private static final ConnectionFactory CONNECTION_FACTORY =
            new ConnectionFactory(DRIVER, H2_URL, USERNAME, PASSWORD);
    private static final CourseDao COURSE_DAO = new CourseDao(CONNECTION_FACTORY);
    private static final GroupDao GROUP_DAO = new GroupDao(CONNECTION_FACTORY);
    private static final StudentDao STUDENT_DAO = new StudentDao(CONNECTION_FACTORY);

    @BeforeAll
    static void setup() {
        createTestData();
        insertTestDataToDB();
    }

    @Test
    void shouldGetCourseIDByName() {
        int expected = 1;
        int actual = 0;

        try {
            actual = COURSE_DAO.getCourseIDByName(COURSE_NAME);
        } catch (ExceptionDao throwables) {
            throwables.printStackTrace();
        }

        assertEquals(expected, actual);
    }

    private static void createTestData() {
        TABLES.create(CONNECTION_FACTORY);

        GROUP.add(new Group(1, "LR-19"));
        courses = DATA.generateCourses();

        List<Course> threeCourses = new LinkedList<>();
        threeCourses.add(courses.get(0));
        threeCourses.add(courses.get(1));
        threeCourses.add(courses.get(2));

        STUDENT.add(new Student(1, GROUP.get(0), "James", "Hetfield", threeCourses));
    }

    private static void insertTestDataToDB() {
        try {
            GROUP_DAO.insertToDB(GROUP);
        } catch (ExceptionDao throwables) {
            throwables.printStackTrace();
        }

        try {
            COURSE_DAO.insertToDB(courses);
        } catch (ExceptionDao throwables) {
            throwables.printStackTrace();
        }

        try {
            STUDENT_DAO.insertToDB(STUDENT);
        } catch (ExceptionDao throwables) {
            throwables.printStackTrace();
        }

        try {
            STUDENT_DAO.assignAllStudentsToCourses(STUDENT);
        } catch (ExceptionDao throwables) {
            throwables.printStackTrace();
        }
    }
}
