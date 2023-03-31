package com.example.hrmrestapi.dto;


import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.util.Experience;
import com.example.hrmrestapi.util.Position;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class EmployeeDTO {

    @NotNull
    @Size(min = 2, max = 25, message = "The employee name must have between 2 and 25 letters")
    private String name;


    @NotNull
    @Size(min = 2, max = 25, message = "The surname must have between 2 and 25 letters")
    private String surname;


    @NotNull
    @Enumerated(EnumType.STRING)
    private Position position;


    @NotNull
    @Enumerated(EnumType.STRING)
    private Experience experience;


    @NotNull
    @Temporal(TemporalType.DATE)
    private Date hiredAt;

    private List<Project> projects;
}
