package com.foxminded.SQL.domain;

public class Course {
    private static final String TAB = " ";

    private final int courseID;
    private final String courseName;
    private final String courseDescription;

    public Course(int courseID, String courseName, String courseDescription) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public String toString() {
        return courseID + TAB + courseName + TAB + courseDescription;
    }
}
