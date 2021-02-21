package com.foxminded.SQL.dao;

public class Queries {
    public static final String CREATE_COURSES_SQL =
            "INSERT INTO courses (course_id, course_name, course_description) VALUES (?, ?, ?)";
    public static final String CREATE_GROUPS_SQL = "INSERT INTO groups (group_id, group_name) VALUES (?, ?)";
    public static final String CREATE_STUDENTS_SQL =
            "INSERT INTO students (student_id, group_id, first_name, last_name) VALUES (?, ?, ?, ?)";
    public static final String CREATE_STUDENTS_AND_COURSES_RELATION_SQL =
            "INSERT INTO studentsAndCoursesRelation (student_id, course_id) VALUES (?, ?)";

    public static final String SELECT_ALL_COURSES_SQL = "SELECT * FROM courses";
    public static final String SELECT_ALL_GROUPS_SQL = "SELECT * FROM groups";
    public static final String SELECT_ALL_STUDENTS_SQL = "SELECT * FROM students";
    public static final String SELECT_ALL_STUDENTS_AND_COURSES_RELATION_SQL =
            "SELECT * FROM studentsAndCoursesRelation";

    public static final String DELETE_STUDENT_FROM_STUDENTS_AND_COURSES_RELATION_BY_ID =
            "DELETE FROM studentsAndCoursesRelation WHERE student_id = ?";
    public static final String DELETE_STUDENT_FROM_STUDENTS_BY_ID = "DELETE FROM students WHERE student_id = ?";
    public static final String DELETE_STUDENT_FROM_COURSE_SQL =
            "DELETE FROM studentsAndCoursesRelation WHERE student_id = ? AND course_id = ?";
}
