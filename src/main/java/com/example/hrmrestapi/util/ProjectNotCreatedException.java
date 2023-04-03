package com.example.hrmrestapi.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProjectNotCreatedException  extends  RuntimeException{
    public ProjectNotCreatedException(String message) {
        super(message);
    }
}
