package com.foxminded.SQL.dao;

public class ExceptionDao extends RuntimeException {

    public ExceptionDao(String message) {
        super(message);
    }
}
