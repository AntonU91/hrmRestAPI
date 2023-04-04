package com.example.hrmrestapi.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.stereotype.Component;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity(name = "Project")
@Table(name = "project")
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "name")
    @NotNull
    @Size(min = 2, max = 25, message = "The project name must have between 2 and 25 letters")
    private String name;

    @Column(name = "launched_at")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date launchedAt;

    @ManyToOne()
    @JoinColumn(name = "project_manager_id", referencedColumnName = "id")
    private ProjectManager projectManager;

    @ManyToMany
    @JoinTable(
            name = "project_employee",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @JsonBackReference
    private List<Employee> employees;

}
