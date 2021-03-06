package com.foxminded.SQL;

import com.foxminded.SQL.dao.ConnectionFactory;
import com.foxminded.SQL.dao.CourseDao;
import com.foxminded.SQL.dao.GroupDao;
import com.foxminded.SQL.dao.StudentDao;
import com.foxminded.SQL.generate.DataGenerator;
import com.foxminded.SQL.generate.SqlRunner;

public class SchoolApplication {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USERNAME = "jhon_doe";
    private static final String PASSWORD = "829893";
    private static final String SCRIPT_FILE = "src\\main\\resources\\create_tables.sql";
    private static final String COURSES_LIST_FILE = "src\\main\\resources\\courses.txt";
    private static final String STUDENT_FIRST_NAMES_LIST_FILE = "src\\main\\resources\\student_first_names.txt";
    private static final String STUDENT_LAST_NAMES_LIST_FILE = "src\\main\\resources\\student_last_names.txt";

    public static void main(String[] args) {
        SqlRunner tables = new SqlRunner(SCRIPT_FILE);
        DataGenerator data =
                new DataGenerator(COURSES_LIST_FILE, STUDENT_FIRST_NAMES_LIST_FILE, STUDENT_LAST_NAMES_LIST_FILE);
        ConnectionFactory connectionFactory =
                new ConnectionFactory(DRIVER, POSTGRES_URL, USERNAME, PASSWORD);
        CourseDao courseDao = new CourseDao(connectionFactory);
        GroupDao groupDao = new GroupDao(connectionFactory);
        StudentDao studentDao = new StudentDao(connectionFactory);

        SchoolApplicationFacade app =
                new SchoolApplicationFacade(tables, data, courseDao,
                groupDao, studentDao, connectionFactory);

        app.run();
    }
}
