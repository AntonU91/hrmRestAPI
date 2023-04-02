package com.example.hrmrestapi.util;

public class InvalidPositionException  extends RuntimeException{
    public InvalidPositionException(String message) {
        super(message);
    }
}
