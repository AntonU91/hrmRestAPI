package com.example.hrmrestapi.util;

public class NoAnyEmployeesException extends RuntimeException {
    public NoAnyEmployeesException(String message) {
        super(message);
    }
}
