package com.example.hrmrestapi.repository;

import com.example.hrmrestapi.model.ProjectManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectManagerRepo extends CrudRepository<ProjectManager, Integer> {
}
