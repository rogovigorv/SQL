package com.foxminded.SQL.generate;

import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Group;
import com.foxminded.SQL.domain.Student;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    private static final String DASH = "-";

    public List<Group> generateGroups() {
        List<Group> groups = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            StringBuilder groupNumber = new StringBuilder();
            Random randomNumber = new Random();

            String generatedString = randomNumber.ints(65, 91)
                    .limit(2)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            int generatedNumber = randomNumber.ints(10, 99)
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);

            groupNumber.append(generatedString)
                    .append(DASH)
                    .append(generatedNumber);

            groups.add(new Group(i, groupNumber.toString()));
        }

        return groups;
    }

    public List<Course> generateCourses() {
        List<Course> courses = new ArrayList<>();

        courses.add(new Course(1,"math",
                "Here you are taught to count."));
        courses.add(new Course(2,"biology",
                "Here you are taught to love animals."));
        courses.add(new Course(3,"chemistry",
                "Here you are taught to cook."));
        courses.add(new Course(4,"english",
                "Here you are taught the right language."));
        courses.add(new Course(5,"geography",
                "Here you are taught how not to get lost."));
        courses.add(new Course(6,"geometry",
                "Here you are taught what is parallel for you."));
        courses.add(new Course(7,"history",
                "Here you are taught what will never happen again."));
        courses.add(new Course(8, "literature",
                "Here you are taught something that never happened."));
        courses.add(new Course(9,"physics",
                "Here you are taught what can happen if you do not know physics well."));
        courses.add(new Course(10,"art",
                "Here you are taught to dream."));

        return courses;
    }

    public List<Student> generateStudents(List<Group> groups, List<Course> courses) {
        List<Student> students = new ArrayList<>();

        for(int i = 1; i <= 200; i++) {

            students.add(new Student(i, groups.get(randomWithinTen() - 1),
                    getFirstName().get(nameRandom()), getLastName().get(nameRandom()), courseRandom(courses)));
        }

        return students;
    }

    private List<String> getFirstName() {
        return Arrays.asList(
                "Olivia", "Isabella", "Evie", "Harry", "Oscar", "James", "Henry", "Leo", "David", "Stanley",
                "Rose", "Alice", "Sophia", "Freya", "Thomas", "George", "John", "Dexter", "Connor", "Owen");
    }

    private List<String> getLastName() {
        return Arrays.asList(
                "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
                "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Wood", "Lewis", "Scott");
    }

    private Integer nameRandom() {
        return (int) (Math.random() * 20);
    }

    private Integer randomWithinTen() {
        return (int) (Math.random() * 10) + 1;
    }

    private List<Course> courseRandom(List<Course> courses) {
        List<Course> result = new LinkedList<>();
        int randomCount = (int) (Math.random() * 3) + 1;

        for (int i = 1; i <= randomCount; i++) {
            int randomCourse = randomWithinTen() - 1;
            if (!result.contains(courses.get(randomCourse))) {
                result.add(courses.get(randomCourse));
            }
        }

        return result;
    }
}
