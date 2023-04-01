package com.example.hrmrestapi.service;

import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.repository.EmployeeRepo;
import com.example.hrmrestapi.util.EmployeeNotFoundException;
import com.example.hrmrestapi.util.NoAnyEmployeesException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {
    EmployeeRepo employeeRepo;


    public Employee findById(int id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        String.format("There is no any employee with id %d", id)));
    }

    public void save(Employee employee) {
        employeeRepo.save(supplementProject(employee));
    }

    public Employee supplementProject(Employee employee) {
        employee.setHiredAt(new Date());
        return employee;
    }

    public List<Employee> findAll() {
        List<Employee> employees = (List<Employee>) employeeRepo.findAll();
        if (employees.isEmpty()) {
            throw new NoAnyEmployeesException("There is no employee exists");
        }
        return employees;
    }
}
