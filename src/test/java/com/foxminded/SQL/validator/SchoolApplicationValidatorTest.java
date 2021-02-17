package com.foxminded.SQL.validator;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SchoolApplicationValidatorTest {
    private static final int ZERO = 0;
    private static final int ELEVEN = 11;
    private static final int ONE_HUNDRED_NINETY_NINE = 199;
    private static final int TWO_HUNDRED_AND_ONE = 201;
    private static final String INCORRECT_COURSE_NAME = "music";
    private static final String INCORRECT_CATEGORY = "xdgvjdh";

    private final Validator validator = new SchoolApplicationValidator();

    @Test
    void validatorShouldThrowIllegalArgumentExceptionWhenArgumentIsLessThenTheRangeOfOneToTen() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                validator.validateFromOneToTen(ZERO));
        assertThat("ID must be in the range from 1 to 10.", equalTo(exception.getMessage()));
    }

    @Test
    void validatorShouldThrowIllegalArgumentExceptionWhenArgumentIsGreaterThenTheRangeOfOneToTen() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                validator.validateFromOneToTen(ELEVEN));
        assertThat("ID must be in the range from 1 to 10.", equalTo(exception.getMessage()));
    }

    @Test
    void validatorShouldThrowIllegalArgumentExceptionWhenArgumentIsLessThenTheRangeOfOneToTwoHundred() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                validator.validateFromOneToTwoHundred(ZERO));
        assertThat("ID must be in the range from 1 to 200.", equalTo(exception.getMessage()));
    }

    @Test
    void validatorShouldThrowIllegalArgumentExceptionWhenArgumentIsGreaterThenTheRangeOfOneToTwoHundred() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                validator.validateFromOneToTwoHundred(TWO_HUNDRED_AND_ONE));
        assertThat("ID must be in the range from 1 to 200.", equalTo(exception.getMessage()));
    }

    @Test
    void validatorShouldThrowIllegalArgumentExceptionWhenArgumentIsLessThenTwoHundred() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                validator.validateMoreThanTwoHundred(ONE_HUNDRED_NINETY_NINE));
        assertThat("ID must be more than 200.", equalTo(exception.getMessage()));
    }

    @Test
    void validatorShouldThrowIllegalArgumentExceptionWhenIncorrectCourseNameIsPassed() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                validator.validateCorrectCourseName(INCORRECT_COURSE_NAME));
        assertThat("This course is not taught here.", equalTo(exception.getMessage()));
    }

    @Test
    void validatorShouldThrowIllegalArgumentExceptionWhenIncorrectCategoryISPassed() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                validator.validateCategory(INCORRECT_CATEGORY));
        assertThat("Category does not exist.", equalTo(exception.getMessage()));
    }
}
