package com.example.hrmrestapi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class EmployeeErrorResponse {
    String message;
    LocalDateTime createdAt;
}
