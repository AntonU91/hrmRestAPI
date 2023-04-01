package com.example.hrmrestapi.model;

import com.example.hrmrestapi.util.Experience;
import com.example.hrmrestapi.util.Position;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity(name = "Employee")
@Table(name = "employee")
@Component
@NoArgsConstructor
@Data
public class Employee {
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

    @Column(name = "position")
    @NotNull(message = "Employee should has position")
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(name = "experience_level")
    @NotNull(message = "Employee should has experience level")
    @Enumerated(EnumType.STRING)
    private Experience experience;

    @Column(name = "hired_at")
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date hiredAt;

    @ManyToMany(mappedBy = "employees")
    @JsonManagedReference
    @JsonIgnoreProperties("employees")
    private List<Project> projects;

}
