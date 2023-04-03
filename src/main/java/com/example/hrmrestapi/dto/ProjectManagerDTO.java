package com.example.hrmrestapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

    @NoArgsConstructor
    @Data
    public class ProjectManagerDTO {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id", unique = true)
        private int id;

        @Column(name = "name")
        @NotNull
        @Size(min = 2, max = 25, message = "The manager name must have between 2 and 25 letters")
        private String name;

        @Column(name = "surname")
        @NotNull
        @Size(min = 2, max = 25, message = "The manager surname must have between 2 and 25 letters")
        private String surname;

        @Column(name = "hired_at")
        @NotNull
        @Temporal(TemporalType.DATE)
        private Date hiredAt;
}
