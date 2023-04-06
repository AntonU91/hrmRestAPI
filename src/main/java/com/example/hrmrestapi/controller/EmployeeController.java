package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.EmployeeDTO;
import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.service.EmployeeService;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
@AllArgsConstructor
public class EmployeeController {
    EmployeeService employeeService;
    ModelMapper modelMapper;

    @GetMapping()
    public List<EmployeeDTO> getAllEmployees() {
        return convertToEmployeeDTO(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployee(@PathVariable(name = "id") int id) {
        return convertToEmployeeDTO(employeeService.findById(id));
    }

    @PutMapping("/{id}/position")
    public ResponseEntity<HttpStatus> updatePosition(@PathVariable(value = "id") int id,
                                                     @RequestBody Map<String, String> positionName) {
        String position = positionName.get("position");
        if (doesPassedPositionMatchAvailablePositions(position)) {
            Employee employeeToChange = employeeService.findById(id);
            employeeToChange.setPosition(Position.valueOf(position.toUpperCase()));
            employeeService.save(employeeToChange);
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            throw new InvalidPositionException("Invalid position name");
        }
    }

    @PutMapping("/{id}/experience")
    public ResponseEntity<HttpStatus> updateExperience(@PathVariable(value = "id") int id,
                                                       @RequestBody Map<String, String> experienceLevel) {
        String experience = experienceLevel.get("experience");
        if (doesPassedExperienceMatchAvailableExpLevel(experience)) {
            Employee employeeToChange = employeeService.findById(id);
            employeeToChange.setExperience(Experience.valueOf(experience.toUpperCase()));
            employeeService.save(employeeToChange);
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            throw new InvalidExperienceLevel("Invalid experience level");
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                stringBuilder.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new EmployeeNotCreatedException(stringBuilder.toString());
        }
        employeeService.save(convertToEmployee(employeeDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable(value = "id") int id) {
        Employee employeeToDelete = employeeService.findById(id);
        if (!employeeToDelete.getProjects().isEmpty()) {
            for (Project project : employeeToDelete.getProjects()) {
                project.getEmployees().remove(employeeToDelete);
            }
        }
        employeeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(NoAnyEmployeesException.class)
    public ResponseEntity<EmployeeErrorResponse> handleException(NoAnyEmployeesException ex) {
        EmployeeErrorResponse employeeErrorResponse = new EmployeeErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(employeeErrorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(EmployeeNotCreatedException.class)
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeNotCreatedException ex) {
        EmployeeErrorResponse employeeErrorResponse = new EmployeeErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(employeeErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeNotFoundException ex) {
        EmployeeErrorResponse employeeErrorResponse = new EmployeeErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(employeeErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPositionException.class)
    public ResponseEntity<EmployeeErrorResponse> handleException(InvalidPositionException ex) {
        EmployeeErrorResponse employeeErrorResponse = new EmployeeErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(employeeErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidExperienceLevel.class)
    public ResponseEntity<EmployeeErrorResponse> handleException(InvalidExperienceLevel ex) {
        EmployeeErrorResponse employeeErrorResponse = new EmployeeErrorResponse(
                ex.getMessage(), LocalDateTime.now()
        );
        return new ResponseEntity<>(employeeErrorResponse, HttpStatus.BAD_REQUEST);
    }


    public EmployeeDTO convertToEmployeeDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public Employee convertToEmployee(EmployeeDTO employeeDTO) {
        return modelMapper.map(employeeDTO, Employee.class);
    }

    public List<EmployeeDTO> convertToEmployeeDTO(List<Employee> employees) {
        return employees
                .stream()
                .map(this::convertToEmployeeDTO).collect(Collectors.toList());
    }

    public boolean doesPassedPositionMatchAvailablePositions(String positionName) {
        for (Position position : Position.values()) {
            if (positionName.toUpperCase().equals(position.name())) {
                return true;
            }
        }
        return false;
    }

    public boolean doesPassedExperienceMatchAvailableExpLevel(String positionName) {
        for (Experience experience : Experience.values()) {
            if (positionName.toUpperCase().equals(experience.name())) {
                return true;
            }
        }
        return false;
    }

}
