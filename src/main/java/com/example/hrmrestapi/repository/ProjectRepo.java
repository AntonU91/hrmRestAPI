package com.example.hrmrestapi.repository;

import com.example.hrmrestapi.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends CrudRepository<Project, Integer> {

}
