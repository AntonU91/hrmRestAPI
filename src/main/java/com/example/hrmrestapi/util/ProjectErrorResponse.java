package com.example.hrmrestapi.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ProjectErrorResponse {
    String message;
    LocalDateTime createdAt;
}
