package com.example.hrmrestapi.repository;

import com.example.hrmrestapi.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends CrudRepository<Employee, Integer> {
}
