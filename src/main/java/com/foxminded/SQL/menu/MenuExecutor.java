package com.foxminded.SQL.menu;

import com.foxminded.SQL.dao.CourseDao;
import com.foxminded.SQL.dao.StudentDao;
import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Group;
import com.foxminded.SQL.domain.Student;
import com.foxminded.SQL.input.Input;
import com.foxminded.SQL.input.QueryInput;
import com.foxminded.SQL.validator.SchoolApplicationValidator;
import com.foxminded.SQL.validator.Validator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuExecutor {
    private final CourseDao courseDao;
    private final StudentDao studentDao;
    private final List<Group> groups;
    private final List<Student> students;
    private final List<Course> courses;

    public MenuExecutor(CourseDao courseDao, StudentDao studentDao, List<Group> groups,
                        List<Student> students, List<Course> courses) {
        this.courseDao = courseDao;
        this.studentDao = studentDao;
        this.groups = groups;
        this.students = students;
        this.courses = courses;
    }

    public void getGroups() {
        Input userChoice = new QueryInput();

        System.out.println("... Please enter any number to find all groups ID with less or equals student count");

        int choice = Integer.parseInt(userChoice.input());

        List<Integer> groupsID = null;

        try {
            groupsID = studentDao.getStudentByGroupID();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Map<Integer, Integer> counts = new HashMap<>();

        if (groupsID != null) {
            for (Integer groupID : groupsID) {
                if (counts.containsKey(groupID)) {
                    counts.put(groupID, counts.get(groupID) + 1);
                } else {
                    counts.put(groupID, 1);
                }
            }
        }

        List<Integer> foundGroups = new ArrayList<>();

        counts.forEach((key, value) -> {
            if (value <= choice) {
                foundGroups.add(key);
            }
        });

        if (foundGroups.isEmpty()) {
            System.out.println("... There are no such groups");
        }

        System.out.print("... Found group IDs: ");
        foundGroups.forEach(g -> System.out.print(g + " "));
    }

    public void addStudent() {
        Input userInput = new QueryInput();
        Validator validator = new SchoolApplicationValidator();

        System.out.println("... Please enter any student ID (must be more than 200)");

        int studentID = Integer.parseInt(userInput.input());
        validator.validateMoreThanTwoHundred(studentID);

        System.out.println("... Please enter any group ID (must be in the range from 1 to 10)");

        int groupID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTen(groupID);

        System.out.println("... Please enter student's first name");

        String firstName = userInput.input();

        System.out.println("... Please enter student's last name");

        String lastName = userInput.input();

        for (Group group : groups) {
            if(groupID == group.getGroupID()) {
                Student student = new Student(studentID, group, firstName, lastName, new ArrayList<>());

                try {
                    studentDao.addNewStudent(student);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        System.out.println("... New student added");
    }

    public void deleteStudent() {
        Input userInput = new QueryInput();
        Validator validator = new SchoolApplicationValidator();

        System.out.println("... Please enter any student ID (ID must be less or equal than 200)");

        int studentID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTwoHundred(studentID);

        for (Student student : students) {
            if (studentID == student.getStudentID()) {
                try {
                    studentDao.deleteStudentByID(studentID);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        System.out.println("... Student deleted");
    }

    public void getAllStudentsByCourseName() {
        Input userInput = new QueryInput();
        Validator validator = new SchoolApplicationValidator();

        System.out.println("... Please enter the course name to find all students enrolled in it " + "\n" +
                "(available courses: math, biology, chemistry, english, geography, geometry, history, " + "\n" +
                "literature, physics, art)");

        String courseName = userInput.input();
        validator.validateCorrectCourseName(courseName);

        List<Integer> studentsID = new ArrayList<>();
        List<Student> studentsSorted = new ArrayList<>();

        for (Course course : courses) {
            if (courseName.equals(course.getCourseName())) {
                try {
                    int courseID = courseDao.getCourseIDByName(courseName);
                    studentsID.addAll(studentDao.getStudentsIDByCourseID(courseID));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }

        for (Integer studentID : studentsID) {
            for (Student student : students) {
                if (studentID == student.getStudentID()) {
                    studentsSorted.add(student);
                }
            }
        }

        studentsSorted.forEach(System.out::println);
    }

    public void addStudentToTheCourse() {
        Input userInput = new QueryInput();
        Validator validator = new SchoolApplicationValidator();

        System.out.println("... Please enter the ID of the student (ID must be less or equal than 200)");

        int studentID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTwoHundred(studentID);

        System.out.println("... Please enter the ID of the course you want to enroll students " +
                "(must be in the range from 1 to 10)");

        int courseID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTen(courseID);

        try {
            studentDao.addToTheCourse(studentID, courseID, students, courses);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("... Student added to the course");
    }

    public void removeTheStudentFromCourse() {
        Input userInput = new QueryInput();
        Validator validator = new SchoolApplicationValidator();

        System.out.println("... Please enter the ID of the student (ID must be less or equal than 200)");

        int studentID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTwoHundred(studentID);

        System.out.println("... Please enter the ID of the course you want to enroll students "  +
                "(must be in the range from 1 to 10)");

        int courseID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTen(courseID);

        try {
            studentDao.removeFromCourse(studentID, courseID, students);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
