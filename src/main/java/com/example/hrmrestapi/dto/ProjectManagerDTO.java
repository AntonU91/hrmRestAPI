package com.example.hrmrestapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@Data
@JsonFormat
public class ProjectManagerDTO {

    private int id;

    @NotNull
    @Size(min = 2, max = 25, message = "The manager name must have between 2 and 25 letters")
    private String name;

    @NotNull
    @Size(min = 2, max = 25, message = "The manager surname must have between 2 and 25 letters")
    private String surname;

    private Date hiredAt;
}
