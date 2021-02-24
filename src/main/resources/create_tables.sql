DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS StudentsAndCoursesRelation CASCADE;

CREATE TABLE groups(
group_id int PRIMARY KEY,
group_name varchar NOT NULL
);

CREATE TABLE students(
student_id int PRIMARY KEY,
group_id int,
first_name varchar NOT NULL,
last_name varchar NOT NULL,
FOREIGN KEY (group_id) REFERENCES groups (group_id)
);

CREATE TABLE courses(
course_id int PRIMARY KEY,
course_name varchar NOT NULL,
course_description varchar NOT NULL
);

CREATE TABLE studentsAndCoursesRelation(
student_id int,
course_id int,
FOREIGN KEY (student_id) REFERENCES students (student_id),
FOREIGN KEY (course_id) REFERENCES courses (course_id)
);
