package com.example.hrmrestapi.util;

public class ProjectNotCreatedException  extends  RuntimeException{
    public ProjectNotCreatedException(String message) {
        super(message);
    }
}
