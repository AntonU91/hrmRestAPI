package com.example.hrmrestapi.util;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ProjectManagerErrorResponse {
    String message;
    LocalDateTime createdAt;
}
