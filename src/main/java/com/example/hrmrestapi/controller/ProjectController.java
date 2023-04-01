package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.ProjectDTO;
import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.service.EmployeeService;
import com.example.hrmrestapi.service.ProjectService;
import com.example.hrmrestapi.util.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {
    ProjectService projectService;
    EmployeeService employeeService;
    ModelMapper modelMapper;

    @GetMapping()
    public List<ProjectDTO> getAllProjects() {
        return convertToProjectDTO(projectService.findAll());
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createProject(@RequestBody() @Valid ProjectDTO projectDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                stringBuilder.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new ProjectNotCreatedException(stringBuilder.toString());
        }
        projectService.save(convertToProject(projectDTO));
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{projectId}/employees/{employeeId}")
    public ResponseEntity<HttpStatus> assignEmployee(@PathVariable(value = "projectId") int projectId, @PathVariable(value = "employeeId") int employeeId) {
        Project project = projectService.findById(projectId);
        Employee employee = employeeService.findById(employeeId);
        // project.setEmployees();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ExceptionHandler(NoAnyProjectsException.class)
    public ResponseEntity<ProjectErrorResponse> handleException(NoAnyProjectsException ex) {
        ProjectErrorResponse projectErrorResponse = new ProjectErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(projectErrorResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ProjectNotCreatedException.class)
    public ResponseEntity<ProjectErrorResponse> handleException(ProjectNotCreatedException ex) {
        ProjectErrorResponse projectErrorResponse = new ProjectErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(projectErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ProjectErrorResponse> handleException(ProjectNotFoundException ex) {
        ProjectErrorResponse projectErrorResponse = new ProjectErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(projectErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ProjectErrorResponse> handleException(EmployeeNotFoundException ex) {
        ProjectErrorResponse projectErrorResponse = new ProjectErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(projectErrorResponse, HttpStatus.NOT_FOUND);
    }


    private ProjectDTO convertToProjectDTO(Project project) {
        return modelMapper.map(project, ProjectDTO.class);
    }

    private Project convertToProject(ProjectDTO projectDTO) {
        return modelMapper.map(projectDTO, Project.class);
    }

    private List<ProjectDTO> convertToProjectDTO(List<Project> projects) {
        return projects
                .stream()
                .map(this::convertToProjectDTO).collect(Collectors.toList());
    }


}
