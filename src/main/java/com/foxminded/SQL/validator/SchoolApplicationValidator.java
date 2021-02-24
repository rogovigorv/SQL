package com.foxminded.SQL.validator;

import java.util.Arrays;
import java.util.List;

public class SchoolApplicationValidator implements Validator {

    @Override
    public void validateFromOneToTen(int ID) {
        if(ID < 1 || ID > 10) {
            throw new IllegalArgumentException("ID must be in the range from 1 to 10.");
        }
    }

    @Override
    public void validateFromOneToTwoHundred(int ID) {
        if(ID < 1 || ID > 200) {
            throw new IllegalArgumentException("ID must be in the range from 1 to 200.");
        }
    }

    @Override
    public void validateMoreThanTwoHundred(int ID) {
        if(ID <= 200) {
            throw new IllegalArgumentException("ID must be more than 200.");
        }
    }

    @Override
    public void validateCorrectCourseName(String courseName) {
        List<String> validCourseNames = Arrays.asList("math", "biology", "chemistry", "english",
                "geography", "geometry", "history", "literature", "physics", "art");

        if (!validCourseNames.contains(courseName)) {
            throw new IllegalArgumentException("This course is not taught here.");
        }
    }

    @Override
    public void validateCategory(String category) {
        List<String> validCategories = Arrays.asList("a", "b", "c", "d", "e", "f");

        if (!validCategories.contains(category)) {
            throw new IllegalArgumentException("Category does not exist.");
        }
    }
}
