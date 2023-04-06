package com.example.hrmrestapi.service;

import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.repository.ProjectRepo;
import com.example.hrmrestapi.util.NoAnyProjectsException;
import com.example.hrmrestapi.util.ProjectNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    ProjectRepo projectRepo;
    EntityManager entityManager;


    public List<Project> findAll() {
        List<Project> projects = (List<Project>) projectRepo.findAll();
        if (projects.isEmpty()) {
            throw new NoAnyProjectsException("There are no projects exist");
        }
        return projects;
    }

    @Transactional
    public Project save(Project project) {
      return   projectRepo.save(supplementProject(project));
    }

    public Project findById(int id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(
                        String.format("There is no project with id %d", id)));

    }

    public Project supplementProject(Project project) {
        if (!projectRepo.existsById(project.getId())) {
            project.setLaunchedAt(new Date());
            project.setEmployees(new ArrayList<>());
        }
        return project;
    }


    public void deleteById(int id) {
        findById(id);
        projectRepo.deleteById(id);
    }
}
