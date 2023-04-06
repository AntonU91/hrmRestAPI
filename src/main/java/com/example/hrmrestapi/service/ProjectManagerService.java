package com.example.hrmrestapi.service;

import com.example.hrmrestapi.model.ProjectManager;
import com.example.hrmrestapi.repository.ProjectManagerRepo;
import com.example.hrmrestapi.util.NoAnyProjectManagersException;
import com.example.hrmrestapi.util.ProjectManagerNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProjectManagerService {
    ProjectManagerRepo projectManagerRepo;


    public List<ProjectManager> findAll() {
        List<ProjectManager> projectManagers = (List<ProjectManager>) projectManagerRepo.findAll();
        if (projectManagers.isEmpty()) {
            throw new NoAnyProjectManagersException("There are no projects");
        }
        return projectManagers;
    }

    public ProjectManager findById(int id) {
        return projectManagerRepo.findById(id).orElseThrow(() -> new ProjectManagerNotFoundException(
                String.format("There is no project manage with id %d", id)));
    }

    @Transactional
    public ProjectManager save(ProjectManager projectManager) {
        return projectManagerRepo.save(supplementProjectManager(projectManager));
    }

    private ProjectManager supplementProjectManager(ProjectManager projectManager) {
        if (!projectManagerRepo.existsById(projectManager.getId())) {
            projectManager.setHiredAt(new Date());
        }
        return projectManager;
    }

    @Transactional
    public void deleteById(int id) {
        findById(id);
        projectManagerRepo.deleteById(id);

    }

}
