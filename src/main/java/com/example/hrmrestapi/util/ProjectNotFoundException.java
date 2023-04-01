package com.example.hrmrestapi.util;

public class ProjectNotFoundException  extends  RuntimeException{
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
