package com.example.hrmrestapi.dto;


import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.util.Experience;
import com.example.hrmrestapi.util.Position;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class EmployeeDTO {

//   // @Setter(AccessLevel.NONE)
private int id;

    @NotNull
    @Size(min = 2, max = 25, message = "The employee name must have between 2 and 25 letters")
    private String name;


    @NotNull
    @Size(min = 2, max = 25, message = "The surname must have between 2 and 25 letters")
    private String surname;


    @Setter(AccessLevel.NONE)
    @NotNull(message = "Employee should has position")
    private Position position;


    @Setter(AccessLevel.NONE)
    @NotNull(message = "Employee should has experience level")
    private Experience experience;


    private Date hiredAt;


    @NotNull
    private List<Project> projects;


    public void setExperience(String experience) {
        this.experience = Experience.valueOf(experience.toUpperCase());
    }

    public void setPosition(String position) {
        this.position = Position.valueOf(position.toUpperCase());
    }

}

