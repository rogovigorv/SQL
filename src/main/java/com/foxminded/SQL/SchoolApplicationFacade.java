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
import com.foxminded.SQL.input.Input;
import com.foxminded.SQL.input.QueryInput;
import com.foxminded.SQL.query.QueryFactory;
import com.foxminded.SQL.validator.SchoolApplicationValidator;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SchoolApplicationFacade {
    private final TablesGenerator tables;
    private final DataGenerator groups;
    private final DataGenerator courses;
    private final DataGenerator students;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final ConnectionFactory connectionFactory;
    private final QueryFactory queryFactory;

    public SchoolApplicationFacade(TablesGenerator tables, DataGenerator groups,
                                   DataGenerator courses, DataGenerator students,
                                   CourseDao courseDao, GroupDao groupDao, StudentDao studentDao,
                                   ConnectionFactory connectionFactory, QueryFactory queryFactory) {
        this.tables = tables;
        this.groups = groups;
        this.courses = courses;
        this.students = students;
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.connectionFactory = connectionFactory;
        this.queryFactory = queryFactory;
    }

    public void run() {
        tables.create(connectionFactory);

        List<Group> groups = new LinkedList<>(this.groups.generateGroups());
        List<Course> courses = new LinkedList<>(this.courses.generateCourses());
        List<Student> students = new LinkedList<>(this.students.generateStudents(groups, courses));

        try {
            groupDao.insertToDB(groups);
            courseDao.insertToDB(courses);
            studentDao.insertToDB(students);
            studentDao.assignAllStudentsToCourses(students);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Input userChoice = new QueryInput();
        System.out.println(queryFactory.sayHello());
        String choice = userChoice.input();
        new SchoolApplicationValidator().validateCategory(choice);

        if (choice.equals("a")) {
            List<Integer> foundGroups = queryFactory.getGroups(studentDao);

            if (foundGroups.isEmpty()) {
                System.out.println("There were no such groups");
            }

            foundGroups.forEach(g -> System.out.print(g + " "));
        }

        if (choice.equals("b")) {
            List<Student> sorted =
                    queryFactory.getAllStudentsByCourseName(courses, students, courseDao, studentDao);

            sorted.forEach(s -> System.out.println(s.toString()));
            System.out.println();
            System.out.println("Calculation is over");
        }

        if(choice.equals("c")) {
            queryFactory.addStudent(groups, studentDao);
            System.out.println("New student added");
        }

        if(choice.equals("d")) {
            queryFactory.deleteStudent(students, studentDao);
            System.out.println("Student deleted");
        }

        if (choice.equals("e")) {
            queryFactory.addStudentToTheCourse(students, courses, studentDao);
            System.out.println("Student added to the course.");
        }

        if (choice.equals("f")) {
            queryFactory.removeTheStudentFromCourse(students, studentDao);
            System.out.println("Student removed from course.");
        }
    }
}
