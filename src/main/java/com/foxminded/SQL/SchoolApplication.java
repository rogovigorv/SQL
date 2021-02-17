package com.foxminded.SQL;

import com.foxminded.SQL.dao.ConnectionFactory;
import com.foxminded.SQL.dao.CourseDao;
import com.foxminded.SQL.dao.GroupDao;
import com.foxminded.SQL.dao.StudentDao;
import com.foxminded.SQL.generate.DataGenerator;
import com.foxminded.SQL.generate.TablesGenerator;

public class SchoolApplication {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String POSTGRES_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USERNAME = "jhon_doe";
    private static final String PASSWORD = "829893";
    private static final String SCRIPT_FILE = "src\\main\\resources\\create_tables.sql";
    private static final String COURSES_LIST_FILE = "src\\main\\resources\\courses.txt";

    public static void main(String[] args) {
        TablesGenerator tables = new TablesGenerator(SCRIPT_FILE);
        DataGenerator data = new DataGenerator(COURSES_LIST_FILE);
        ConnectionFactory connectionFactory = new ConnectionFactory(DRIVER, POSTGRES_URL, USERNAME, PASSWORD);
        CourseDao courseDao = new CourseDao(connectionFactory);
        GroupDao groupDao = new GroupDao(connectionFactory);
        StudentDao studentDao = new StudentDao(connectionFactory);

        SchoolApplicationFacade launch =
                new SchoolApplicationFacade(tables, data, courseDao,
                groupDao, studentDao, connectionFactory);

        launch.run();
    }
}
