package com.example.hrmrestapi.service;

import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.repository.ProjectRepo;
import com.example.hrmrestapi.util.NoAnyProjectsException;
import com.example.hrmrestapi.util.ProjectNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectService {
    ProjectRepo projectRepo;


    public List<Project> findAll() {
        List<Project> projects = (List<Project>) projectRepo.findAll();
        if (projects.isEmpty()) {
            throw new NoAnyProjectsException("There are no projects exist");
        }
        return projects;
    }

    public void save(Project project) {
        projectRepo.save(supplementProject(project));
    }

    public Project findById(int id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(
                        String.format("There is no project with id %d", id)));

    }

    public Project supplementProject(Project project) {
        project.setLaunchedAt(new Date());
        return project;
    }
}
