package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.EmployeeDTO;
import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.service.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.beans.BeanProperty;

@RestController()
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {
    EmployeeService employeeService;
    ModelMapper modelMapper;

    @PostMapping
    public void create(@RequestBody @Valid EmployeeDTO employeeDTO, BindingResult bindingResult) {
     //   if()

    }

    public EmployeeDTO convertToEmployeeDTO(Employee employee) {
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public Employee convertToEmployee(EmployeeDTO employeeDTO) {
        return modelMapper.map(employeeDTO, Employee.class);
    }



}
