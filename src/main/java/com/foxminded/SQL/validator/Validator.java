package com.foxminded.SQL.validator;

public interface Validator {

    void validateFromOneToTen(int ID);

    void validateFromOneToTwoHundred(int ID);

    void validateCorrectCourseName(String courseName);

    void validateCategory(String category);
}

