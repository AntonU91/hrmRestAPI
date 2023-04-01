package com.example.hrmrestapi.service;

import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.repository.EmployeeRepo;
import com.example.hrmrestapi.util.EmployeeNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmployeeService {
    EmployeeRepo employeeRepo;


    public Employee findById(int id) {
      return   employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        String.format("There is no any employee with id %d", id)));
    }
}