package com.example.hrmrestapi.util;

public class EmployeeNotCreatedException  extends  RuntimeException{
    public EmployeeNotCreatedException(String message) {
        super(message);
    }
}
