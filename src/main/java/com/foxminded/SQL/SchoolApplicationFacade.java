package com.foxminded.SQL;

import com.foxminded.SQL.dao.CourseDao;
import com.foxminded.SQL.dao.GroupDao;
import com.foxminded.SQL.dao.StudentDao;
import com.foxminded.SQL.dao.StudentsAndCoursesRelationDao;
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
    private final TablesGenerator getTables;
    private final DataGenerator getGroups;
    private final DataGenerator getCourses;
    private final DataGenerator getStudents;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final StudentsAndCoursesRelationDao studentsAndCoursesRelationDao;
    private final QueryFactory queryFactory;

    public SchoolApplicationFacade(TablesGenerator getTables, DataGenerator getGroups,
                                   DataGenerator getCourses, DataGenerator getStudents,
                                   CourseDao courseDao, GroupDao groupDao, StudentDao studentDao,
                                   StudentsAndCoursesRelationDao studentsAndCoursesRelationDao,
                                   QueryFactory queryFactory) {
        this.getTables = getTables;
        this.getGroups = getGroups;
        this.getCourses = getCourses;
        this.getStudents = getStudents;
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.studentsAndCoursesRelationDao = studentsAndCoursesRelationDao;
        this.queryFactory = queryFactory;
    }

    public void run() {
        getTables.create();

        List<Group> groups = new LinkedList<>(getGroups.generateGroups());
        List<Course> courses = new LinkedList<>(getCourses.generateCourses());
        List<Student> students = new LinkedList<>(getStudents.generateStudents(groups, courses));

        try {
            groupDao.insertToDB(groups);
            courseDao.insertToDB(courses);
            studentDao.addAllStudents(students);
            studentsAndCoursesRelationDao.insertToDB(students);
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
                    queryFactory.getAllStudentsByCourseName(courses, students,courseDao, studentsAndCoursesRelationDao);

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
            queryFactory.addStudentToTheCourse(students, courses, studentsAndCoursesRelationDao);
            System.out.println("Student added to the course.");
        }

        if (choice.equals("f")) {
            queryFactory.removeTheStudentFromCourse(students, studentsAndCoursesRelationDao);
            System.out.println("Student removed from course.");
        }
    }
}
