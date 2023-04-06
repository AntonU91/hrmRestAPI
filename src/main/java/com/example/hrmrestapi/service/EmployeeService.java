package com.example.hrmrestapi.service;

import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.repository.EmployeeRepo;
import com.example.hrmrestapi.util.EmployeeNotFoundException;
import com.example.hrmrestapi.util.NoAnyEmployeesException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeService {
    EmployeeRepo employeeRepo;
    EntityManager entityManager;


    public Employee findById(int id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(
                        String.format("There is no any employee with id %d", id)));
    }

    public Employee save(Employee employee) {
        return employeeRepo.save(supplementEmployee(employee));
    }

    public Employee supplementEmployee(Employee employee) {
        if (!employeeRepo.existsById(employee.getId())) {
            employee.setHiredAt(new Date());
            employee.setProjects(new ArrayList<>());
        }
        return employee;
    }

    public List<Employee> findAll() {
        List<Employee> employees = (List<Employee>) employeeRepo.findAll();
        if (employees.isEmpty()) {
            throw new NoAnyEmployeesException("There is no employee exists");
        }
        return employees;
    }

    public void deleteById(int id) {
        findById(id);
        employeeRepo.deleteById(id);
    }
}
