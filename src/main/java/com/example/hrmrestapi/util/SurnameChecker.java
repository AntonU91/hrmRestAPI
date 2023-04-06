package com.example.hrmrestapi.util;

import lombok.AllArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
public class SurnameChecker {
    @Size( max = 0, message = "The project manager surname must have between 2 and 25 letters ")
    String surmame;
}
