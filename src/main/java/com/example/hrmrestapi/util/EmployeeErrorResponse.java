package com.example.hrmrestapi.util;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class EmployeeErrorResponse {
    String message;
    LocalDateTime createdAt;
}
