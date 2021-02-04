package com.foxminded.SQL;

import com.foxminded.SQL.dao.CourseDao;
import com.foxminded.SQL.dao.GroupDao;
import com.foxminded.SQL.dao.StudentDao;
import com.foxminded.SQL.dao.StudentsAndCoursesRelationDao;
import com.foxminded.SQL.generate.DataGenerator;
import com.foxminded.SQL.generate.TablesGenerator;
import com.foxminded.SQL.query.QueryFactory;

public class SchoolApplication {
    public static void main(String[] args) {
        TablesGenerator getTables = new TablesGenerator();
        DataGenerator getGroups = new DataGenerator();
        DataGenerator getCourses = new DataGenerator();
        DataGenerator getStudents = new DataGenerator();
        CourseDao courseDao = new CourseDao();
        GroupDao groupDao = new GroupDao();
        StudentDao studentDao = new StudentDao();
        StudentsAndCoursesRelationDao studentsAndCoursesRelationDao = new StudentsAndCoursesRelationDao();
        QueryFactory queryFactory = new QueryFactory();

        SchoolApplicationFacade launch = new SchoolApplicationFacade(getTables, getGroups, getCourses,
                getStudents, courseDao, groupDao, studentDao, studentsAndCoursesRelationDao, queryFactory);

        launch.run();
    }
}
