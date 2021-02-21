package com.foxminded.SQL.generate;

import com.foxminded.SQL.domain.Course;
import com.foxminded.SQL.domain.Group;
import com.foxminded.SQL.domain.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.LinkedList;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

@ExtendWith(MockitoExtension.class)
public class DataGeneratorTest {
    private static final String COURSES_LIST_FILE = "src\\main\\resources\\courses.txt";

    @Mock
    List<Group> groups;

    @Mock
    List<Course> courses;

    private final DataGenerator data = new DataGenerator(COURSES_LIST_FILE);

    @Test
    void shouldConfirmThatTheReturnedObjectClassInTheListMatchesTheGroupClass() {
        final List<Group> actual = new LinkedList<>(data.generateGroups());

        actual.forEach(a -> assertThat(a, instanceOf(Group.class)));
    }

    @Test
    void shouldConfirmThatTheReturnedObjectClassInTheListMatchesTheCourseClass() {
        final List<Course> actual = new LinkedList<>(data.generateCourses());

        actual.forEach(a -> assertThat(a, instanceOf(Course.class)));
    }

    @Test
    void shouldConfirmThatTheReturnedObjectClassInTheListMatchesTheStudentClass() {
        final List<Student> actual =
                new LinkedList<>(data.generateStudents(groups, courses));

        actual.forEach(a -> assertThat(a, instanceOf(Student.class)));
    }
}
