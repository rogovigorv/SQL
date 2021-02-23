package com.foxminded.SQL.generate;

import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Group;
import com.foxminded.SQL.domain.Student;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

public class DataGenerator {
    private static final String DASH = "-";

    private final String coursesListFile;
    private final String studentFirstNamesListFile;
    private final String studentLastNamesListFile;

    public DataGenerator(String coursesListFile, String studentFirstNamesListFile,
                         String studentLastNamesListFile) {
        this.coursesListFile = coursesListFile;
        this.studentFirstNamesListFile = studentFirstNamesListFile;
        this.studentLastNamesListFile = studentLastNamesListFile;
    }

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
        List<String> readCoursesFromFile = readFromFile(coursesListFile);

        return readCoursesFromFile.stream().map(c -> {
            String[] splitLine = c.split(DASH);
            int courseID = Integer.parseInt(splitLine[0]);
            String courseName = splitLine[1];
            String courseDescription = splitLine[2];

            return new Course(courseID, courseName, courseDescription);
        }).collect(toList());
    }

    public List<Student> generateStudents(List<Group> groups, List<Course> courses) {
        List<Student> students = new ArrayList<>();

        for(int i = 1; i <= 200; i++) {

            students.add(new Student(i, groups.get(randomWithinTen() - 1),
                    readFromFile(studentFirstNamesListFile).get(nameRandom()),
                    readFromFile(studentLastNamesListFile).get(nameRandom()), courseRandom(courses)));
        }

        return students;
    }

//    private List<String> getFirstName() {
//        return Arrays.asList(
//                "Olivia", "Isabella", "Evie", "Harry", "Oscar", "James", "Henry", "Leo", "David", "Stanley",
//                "Rose", "Alice", "Sophia", "Freya", "Thomas", "George", "John", "Dexter", "Connor", "Owen");
//    }
//
//    private List<String> getLastName() {
//        return Arrays.asList(
//                "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
//                "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Wood", "Lewis", "Scott");
//    }

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

    private List<String> readFromFile(String fileAddress) {
        List<String> readFromFile = new ArrayList<>();

        try {
            readFromFile = Files.lines(Paths.get(fileAddress))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return readFromFile;
    }
}
