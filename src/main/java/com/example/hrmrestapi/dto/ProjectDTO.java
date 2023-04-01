package com.example.hrmrestapi.dto;


import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.model.ProjectManager;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class ProjectDTO {

    @NotNull
    @Size(min = 2, max = 25, message = "The project name must have between 2 and 25 letters")
    private String name;

    @Temporal(TemporalType.DATE)
    private Date launchedAt;

    private ProjectManager projectManager;

    private List<Employee> employees;
}
