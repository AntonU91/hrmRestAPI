package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.ProjectManagerDTO;
import com.example.hrmrestapi.model.ProjectManager;
import com.example.hrmrestapi.service.ProjectManagerService;
import com.example.hrmrestapi.util.NoAnyProjectManagersException;
import com.example.hrmrestapi.util.ProjectManagerErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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


    @ExceptionHandler(NoAnyProjectManagersException.class)
    public ResponseEntity<ProjectManagerErrorResponse> handleException(NoAnyProjectManagersException ex) {
        ProjectManagerErrorResponse projectManagerErrorResponse = new ProjectManagerErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(projectManagerErrorResponse, HttpStatus.NOT_FOUND);
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

}
