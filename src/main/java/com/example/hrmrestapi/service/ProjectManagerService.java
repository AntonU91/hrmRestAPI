package com.example.hrmrestapi.service;

import com.example.hrmrestapi.model.ProjectManager;
import com.example.hrmrestapi.repository.ProjectManagerRepo;
import com.example.hrmrestapi.util.NoAnyProjectManagersException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProjectManagerService {
    ProjectManagerRepo projectManagerRepo;

    public List<ProjectManager> findAll() {
        List<ProjectManager> projectManagers = (List<ProjectManager>) projectManagerRepo.findAll();
        if (projectManagers.isEmpty()) {
            throw new NoAnyProjectManagersException("There are no projects");
        }
        return projectManagers;
    }
}
