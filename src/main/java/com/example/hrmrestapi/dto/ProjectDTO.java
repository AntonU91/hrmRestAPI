package com.example.hrmrestapi.dto;


import com.example.hrmrestapi.dto.EmployeeDTO;
import com.example.hrmrestapi.model.ProjectManager;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {

    private int id;

    @NotNull
    @Size(min = 2, max = 25, message = "The project name must have between 2 and 25 letters")
    private String name;


    private Date launchedAt;

    private ProjectManagerDTO projectManagerDTO;

    @JsonIgnoreProperties({"projects"})
    private List<EmployeeDTO> employees;

}
