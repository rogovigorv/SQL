package com.foxminded.SQL.query;

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

public class QueryFactory {

    public String sayHello() {
        return "Press 'a' to find all groups with less or equals student count." + "\n" +
                "Press 'b' to find all students related to course with given name." + "\n" +
                "Press 'c' to add new student." + "\n" +
                "Press 'd' to delete student by STUDENT_ID." + "\n" +
                "Press 'e' to add a student to the course (from a list)." + "\n" +
                "Press 'f' to remove the student from one of his or her courses.";
    }

    public List<Integer> getGroups(StudentDao studentDao) {
        System.out.println("Please enter any number to find all groups ID with less or equals student count.");

        Input userChoice = new QueryInput();
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

        return foundGroups;
    }

    public void addStudent(List<Group> groups, StudentDao studentDao) {
        System.out.println("Please enter any student ID (must be more than 200), group ID " + "\n" +
                "(must be in the range from 1 to 10), first name and last name.");

        Input userInput = new QueryInput();
        Validator validator = new SchoolApplicationValidator();

        int studentID = Integer.parseInt(userInput.input());
        validator.validateMoreThanTwoHundred(studentID);

        int groupID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTen(groupID);

        String firstName = userInput.input();
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
    }

    public void deleteStudent(List<Student> students, StudentDao studentDao) {
        System.out.println("Please enter any student ID (ID must be less or equal than 200).");

        Input userInput = new QueryInput();
        int studentID = Integer.parseInt(userInput.input());
        new SchoolApplicationValidator().validateFromOneToTwoHundred(studentID);

        for (Student student : students) {
            if (studentID == student.getStudentID()) {
                try {
                    studentDao.deleteStudentByID(studentID);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public List<Student> getAllStudentsByCourseName(List<Course> courses, List<Student> students, CourseDao courseDao,
                                                    StudentDao studentDao) {
        System.out.println("Please enter the course name to find all students enrolled in it " + "\n" +
                "(available courses: math, biology, chemistry, english, geography, geometry, history, " + "\n" +
                "literature, physics, art).");

        Input userInput = new QueryInput();
        String courseName = userInput.input();

        new SchoolApplicationValidator().validateCorrectCourseName(courseName);

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

        return studentsSorted;
    }

    public void addStudentToTheCourse(List<Student> students,  List<Course> courses, StudentDao studentDao) {
        System.out.println("Please enter the ID of the student (ID must be less or equal than 200).");

        Input userInput = new QueryInput();
        Validator validator = new SchoolApplicationValidator();

        int studentID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTwoHundred(studentID);

        System.out.println("Please enter the ID of the course you want to enroll students " +
                "(must be in the range from 1 to 10).");

        int courseID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTen(courseID);

        try {
            studentDao.addToTheCourse(studentID, courseID, students, courses);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeTheStudentFromCourse(List<Student> students, StudentDao studentDao) {
        System.out.println("Please enter the ID of the student (ID must be less or equal than 200).");

        Input userInput = new QueryInput();
        Validator validator = new SchoolApplicationValidator();

        int studentID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTwoHundred(studentID);

        System.out.println("Please enter the ID of the course you want to enroll students "  +
                "(must be in the range from 1 to 10).");

        int courseID = Integer.parseInt(userInput.input());
        validator.validateFromOneToTen(courseID);

        try {
            studentDao.removeFromCourse(studentID, courseID, students);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
