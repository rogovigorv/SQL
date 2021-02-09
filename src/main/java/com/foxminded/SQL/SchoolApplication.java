package com.foxminded.SQL;

import com.foxminded.SQL.dao.ConnectionFactory;
import com.foxminded.SQL.dao.CourseDao;
import com.foxminded.SQL.dao.GroupDao;
import com.foxminded.SQL.dao.StudentDao;
import com.foxminded.SQL.generate.DataGenerator;
import com.foxminded.SQL.generate.TablesGenerator;
import com.foxminded.SQL.query.QueryFactory;

public class SchoolApplication {
    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USERNAME = "jhon_doe";
    private static final String PASSWORD = "829893";
    private static final String SCRIPT_FILE = "src\\main\\resources\\create_tables.sql";
    private static final String COURSES_LIST_FILE = "src\\main\\resources\\courses.txt";

    public static void main(String[] args) {
        TablesGenerator tables = new TablesGenerator(SCRIPT_FILE);
        DataGenerator groups = new DataGenerator(COURSES_LIST_FILE);
        DataGenerator courses = new DataGenerator(COURSES_LIST_FILE);
        DataGenerator students = new DataGenerator(COURSES_LIST_FILE);
        ConnectionFactory connectionFactory = new ConnectionFactory(POSTGRES_URL, USERNAME, PASSWORD);
        CourseDao courseDao = new CourseDao(connectionFactory);
        GroupDao groupDao = new GroupDao(connectionFactory);
        StudentDao studentDao = new StudentDao(connectionFactory);
        QueryFactory queryFactory = new QueryFactory();

        SchoolApplicationFacade launch = new SchoolApplicationFacade(tables, groups, courses,
                students, courseDao, groupDao, studentDao, connectionFactory, queryFactory);

        launch.run();
    }
}
