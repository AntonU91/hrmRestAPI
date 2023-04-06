package com.example.hrmrestapi.util;

public class InvalidRequestException  extends RuntimeException{
    public InvalidRequestException(String message) {
        super(message);
    }
}
