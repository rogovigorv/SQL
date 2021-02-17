package com.foxminded.SQL.dao;

import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Group;
import com.foxminded.SQL.domain.Student;
import com.foxminded.SQL.generate.DataGenerator;
import com.foxminded.SQL.generate.TablesGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentDaoTest {
    private static final String DRIVER = "org.h2.Driver";
    private static final String POSTGRES_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";
    private static final String SCRIPT_FILE = "src\\main\\resources\\create_tables.sql";
    private static final String COURSES_LIST_FILE = "src\\main\\resources\\courses.txt";
    private static final int COURSE_ID = 1;

    private static final List<Student> student = new ArrayList<>();
    private static final List<Group> group = new LinkedList<>();
    private static List<Course> courses = new LinkedList<>();

    private static final TablesGenerator tables = new TablesGenerator(SCRIPT_FILE);
    private static final DataGenerator data = new DataGenerator(COURSES_LIST_FILE);
    private static final ConnectionFactory connectionFactory = new ConnectionFactory(DRIVER, POSTGRES_URL, USERNAME, PASSWORD);
    private static final CourseDao courseDao = new CourseDao(connectionFactory);
    private static final GroupDao groupDao = new GroupDao(connectionFactory);
    private static final StudentDao studentDao = new StudentDao(connectionFactory);

    @BeforeAll
    static void setup() {
        createTestData();
        insertTestDataToDB();
    }

    @Test
    void shouldGetStudentByGroupID() {
        List<Integer> expected = Collections.singletonList(1);

        List<Integer> actual = new ArrayList<>();

        try {
           actual = studentDao.getStudentByGroupID();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetStudentsIDByCourseID() {

        Set<Integer> expected = new HashSet<>();
        expected.add(1);

        Set<Integer> actual = new HashSet<>();

        try {
            actual = studentDao.getStudentsIDByCourseID(COURSE_ID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assertEquals(expected, actual);
    }

    private static void createTestData() {
        tables.create(connectionFactory);

        group.add(new Group(1, "LR-19"));
        courses = data.generateCourses();

        List<Course> threeCourses = new LinkedList<>();
        threeCourses.add(courses.get(0));
        threeCourses.add(courses.get(1));
        threeCourses.add(courses.get(2));

        student.add(new Student(1, group.get(0), "James", "Hetfield", threeCourses));
    }

    private static void insertTestDataToDB() {
        try {
            groupDao.insertToDB(group);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            courseDao.insertToDB(courses);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            studentDao.insertToDB(student);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            studentDao.assignAllStudentsToCourses(student);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
