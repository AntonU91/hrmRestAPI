package com.example.hrmrestapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Entity(name="ProjectManager")
@Table(name = "project_manager")
@Component
@NoArgsConstructor
@Getter
@Setter
public class ProjectManager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "name")
    @NotNull
    @Size(min = 2, max = 25, message = "The employee name must have between 2 and 25 letters")
    private String name;

    @Column(name = "surname")
    @NotNull
    @Size(min = 2, max = 25, message = "The surname must have between 2 and 25 letters")
    private String surname;

    @Column(name = "hired_at")
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date hiredAt;
}
