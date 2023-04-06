package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.ProjectManagerDTO;
import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.model.ProjectManager;
import com.example.hrmrestapi.service.ProjectManagerService;
import com.example.hrmrestapi.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/managers")
public class ProjectManagerController {
    ModelMapper modelMapper;
    ProjectManagerService projectManagerService;

    @GetMapping
    public List<ProjectManagerDTO> getAll() {
        return convertToProjectManagerDTO(projectManagerService.findAll());
    }

    @GetMapping("/{id}")
    public ProjectManagerDTO getById(@PathVariable("id") int id) {
        return convertToProjectManagerDTO(projectManagerService.findById(id));

    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ProjectManagerDTO projectManagerDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                stringBuilder.append(fieldError.getField())
                        .append(" - ")
                        .append(fieldError.getDefaultMessage())
                        .append(";");
            }
            throw new ProjectManagerNotCreatedException(stringBuilder.toString());
        }
        projectManagerService.save(convertToProjectManager(projectManagerDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        ProjectManager projectManager = projectManagerService.findById(id);
        for (Project project : projectManager.getProjects()) {
            project.setProjectManager(null);
        }
        projectManagerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/surname")
    public ResponseEntity<HttpStatus> changeSurname(@RequestBody Map<String, String> request,
                                                    @PathVariable(value = "id") int id) {
        String surname = getManagerSurname(request);
        isPassedSurnameValid(surname);
        ProjectManager projectManager = projectManagerService.findById(id);
        projectManager.setSurname(surname);
        projectManagerService.save(projectManager);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(NoAnyProjectManagersException.class)
    public ResponseEntity<ProjectManagerErrorResponse> handleException(NoAnyProjectManagersException ex) {
        ProjectManagerErrorResponse projectManagerErrorResponse = new ProjectManagerErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(projectManagerErrorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ProjectManagerErrorResponse> handleException(ProjectNotFoundException ex) {
        ProjectManagerErrorResponse projectManagerErrorResponse = new ProjectManagerErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(projectManagerErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProjectManagerNotCreatedException.class)
    public ResponseEntity<ProjectManagerErrorResponse> handleException(ProjectManagerNotCreatedException ex) {
        ProjectManagerErrorResponse projectManagerErrorResponse = new ProjectManagerErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(projectManagerErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidProjectManagerNameException.class)
    public ResponseEntity<ProjectManagerErrorResponse> handleException(InvalidProjectManagerNameException ex) {
        ProjectManagerErrorResponse projectManagerErrorResponse = new ProjectManagerErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(projectManagerErrorResponse, HttpStatus.BAD_REQUEST);
    }


    private ProjectManagerDTO convertToProjectManagerDTO(ProjectManager projectManager) {
        return modelMapper.map(projectManager, ProjectManagerDTO.class);
    }

    private ProjectManager convertToProjectManager(ProjectManagerDTO projectManagerDTO) {
        return modelMapper.map(projectManagerDTO, ProjectManager.class);
    }

    private List<ProjectManagerDTO> convertToProjectManagerDTO(List<ProjectManager> projectManagers) {
        return projectManagers
                .stream()
                .map(this::convertToProjectManagerDTO).collect(Collectors.toList());
    }

    private String getManagerSurname(Map<String, String> request) {
        if (request.containsKey("surname")) {
            return request.get("surname");
        } else throw new InvalidRequestException(String.format("Invalid request key \"%s\"", request.keySet()));
    }

    private boolean isPassedSurnameValid(String surname) {
        if (surname.length()<3|| surname.length()>25) {
            throw new InvalidProjectManagerNameException("The project manager surname must have between 3 and 25 letters");
        }
        return true;
    }

}
