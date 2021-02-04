package com.foxminded.SQL.domain;

import java.util.List;

public class Student {
    private static final String TAB = " ";

    private final int studentID;
    private final Group group;
    private final String firstName;
    private final String lastName;
    private final List<Course> courses;

    public Student(int studentID, Group group, String firstName, String lastName, List<Course> courses) {
        this.studentID = studentID;
        this.group = group;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = courses;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Group getGroup() {
        return group;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public String toString() {
        return firstName + TAB + lastName;
    }
}
