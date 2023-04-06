package com.example.hrmrestapi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ProjectManagerErrorResponse {
    String message;
    LocalDateTime createdAt;
}
