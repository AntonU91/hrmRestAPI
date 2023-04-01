package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.EmployeeDTO;
import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.service.EmployeeService;
import com.example.hrmrestapi.util.EmployeeNotCreatedException;
import com.example.hrmrestapi.util.EmployeeErrorResponse;
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

@RestController()
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {
    EmployeeService employeeService;
    ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid EmployeeDTO employeeDTO, BindingResult bindingResult) {
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
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @ExceptionHandler(EmployeeNotCreatedException.class)
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeNotCreatedException ex) {
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


}
