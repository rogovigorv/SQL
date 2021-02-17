package com.foxminded.SQL;

import com.foxminded.SQL.dao.ConnectionFactory;
import com.foxminded.SQL.dao.CourseDao;
import com.foxminded.SQL.dao.GroupDao;
import com.foxminded.SQL.dao.StudentDao;
import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Group;
import com.foxminded.SQL.domain.Student;
import com.foxminded.SQL.generate.DataGenerator;
import com.foxminded.SQL.generate.TablesGenerator;
import com.foxminded.SQL.menu.Menu;
import com.foxminded.SQL.menu.MenuItem;
import com.foxminded.SQL.menu.MenuExecutor;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SchoolApplicationFacade {
    private final TablesGenerator tables;
    private final DataGenerator data;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final ConnectionFactory connectionFactory;

    public SchoolApplicationFacade(TablesGenerator tables, DataGenerator data,
                                   CourseDao courseDao, GroupDao groupDao,
                                   StudentDao studentDao, ConnectionFactory connectionFactory) {
        this.tables = tables;
        this.data = data;
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.connectionFactory = connectionFactory;
    }

    public void run() {
        tables.create(connectionFactory);

        List<Group> groups = new LinkedList<>(this.data.generateGroups());
        List<Course> courses = new LinkedList<>(this.data.generateCourses());
        List<Student> students = new LinkedList<>(this.data.generateStudents(groups, courses));

        try {
            groupDao.insertToDB(groups);
            courseDao.insertToDB(courses);
            studentDao.insertToDB(students);
            studentDao.assignAllStudentsToCourses(students);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        MenuExecutor menuExecutor = new MenuExecutor(courseDao, studentDao, groups, students, courses);

        Menu menu = new Menu();

        menu.addItem(new MenuItem("Press '1' to find all groups with less or equals student count",
                menuExecutor, "getGroups"));
        menu.addItem(new MenuItem("Press '2' to find all students related to course with given name",
                menuExecutor, "getAllStudentsByCourseName"));
        menu.addItem(new MenuItem("Press '3' to add new student",
                menuExecutor, "addStudent"));
        menu.addItem(new MenuItem("Press '4' to delete student by STUDENT_ID",
                menuExecutor, "deleteStudent"));
        menu.addItem(new MenuItem("Press '5' to add a student to the course (from a list)",
                menuExecutor, "addStudentToTheCourse"));
        menu.addItem(new MenuItem("Press '6' to remove the student from one of his or her courses",
                menuExecutor, "removeTheStudentFromCourse"));

        menu.execute();
    }
}
