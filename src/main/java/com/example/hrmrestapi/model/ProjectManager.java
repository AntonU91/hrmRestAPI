package com.example.hrmrestapi.model;

import jdk.dynalink.linker.LinkerServices;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity(name="ProjectManager")
@Table(name = "project_manager")
@Component
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ProjectManager {
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

    @OneToMany(mappedBy = "projectManager" )
    List<Project> projects;
}
