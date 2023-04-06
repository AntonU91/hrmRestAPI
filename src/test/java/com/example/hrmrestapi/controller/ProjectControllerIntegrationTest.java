package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.ProjectDTO;
import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.model.ProjectManager;
import com.example.hrmrestapi.repository.ProjectRepo;
import com.example.hrmrestapi.service.EmployeeService;
import com.example.hrmrestapi.service.ProjectManagerService;
import com.example.hrmrestapi.service.ProjectService;
import com.example.hrmrestapi.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional()
public class ProjectControllerIntegrationTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectController projectController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ProjectManagerService projectManagerService;


    @Sql(scripts = "/test.sql")
    @BeforeEach
    void setUp() {
        projectRepo.deleteAll();
        Project project1 = Project.builder()
                .name("First")
                .build();
        Project project2 = Project.builder()
                .name("Second")
                .build();
        projectService.save(project1);
        projectService.save(project2);
        Employee employee = Employee.builder()
                .name("Anton")
                .surname("Uzhva")
                .experience(Experience.JUNIOR)
                .position(Position.DEVOPS)
                .hiredAt(new Date())
                .build();
        employeeService.save(employee);
    }

    @Test
    void whenGetAllProjectsThanReturnSpecifiedListOfProjectsAndStatus200() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        List<ProjectDTO> actualList = objectMapper.readValue(response, new TypeReference<List<ProjectDTO>>() {
        });

        List<Project> expectedList = projectService.findAll();
        assertAll(() -> {
            assertEquals(expectedList.size(), actualList.size());
            assertEquals(expectedList.get(0).getId(), actualList.stream()
                    .map(projectDTO -> modelMapper.map(projectDTO, Project.class))
                    .collect(Collectors.toList()).get(0).getId());
        });
    }

    @Test
    void whenTryGetAllProjectsAndTheAreNoProjectsThanThrowNoAnyProjectsExceptionAndStatus404() throws Exception {
        projectRepo.deleteAll();
        MvcResult mvcResult = mockMvc.perform(get("/projects"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThrows(NoAnyProjectsException.class, projectService::findAll);
    }

    @Test
    void whenGetProjectByIdThanReturnProperOne() throws Exception {
        List<Project> list = projectService.findAll();   //
        Project project = Project.builder()
                .name("Test proj")
                .build();
        projectService.save(project);
        MvcResult mvcResult = mockMvc.perform(get("/projects/{id}", project.getId()))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        ProjectDTO actualProj = objectMapper.readValue(response, ProjectDTO.class);
        Project expectedlProj = projectService.findById(project.getId());
        assertEquals(expectedlProj.getId(), modelMapper.map(actualProj, Project.class).getId());
    }

    @Test
    void whenGetProjectByIdAndThereAreNoProjectWithSuchIdThanReturnProjectNotFoundException() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/projects/{id}", 100000))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThrows(ProjectNotFoundException.class, () -> projectService.findById(10000));
    }


    @Test
    void whenCreateProjectThanSaveAndReturnStatus201() throws Exception {
        ProjectDTO projectDTO = ProjectDTO.builder()
                .name("Test project")
                .build();
        List<Project> projectList = projectService.findAll();
        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(status().isCreated());
        List<Project> expectedProjList = projectService.findAll();
        assertEquals(projectList.size() + 1, expectedProjList.size());

    }

    @Test
    void whenTryToCreateProjWithInvalidFieldsThanReturnStatus400() throws Exception {
        ProjectDTO invalidProjDTO = ProjectDTO
                .builder().build();
        MvcResult mvcResult = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProjDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void whenAddEmployeeToProjectThatReturnThisProjectWhereEmployeeWasAddedContainThisOne() throws Exception {
        List<Project> projects = projectService.findAll();// FOR TEST!!!
        Project project = Project.builder()
                .name("Second")
                .build();
        Project savedProject = projectService.save(project);
        int projectId = savedProject.getId();

        Employee employee = Employee.builder()
                .name("Anton")
                .surname("Uzhva")
                .experience(Experience.JUNIOR)
                .position(Position.DEVOPS)
                .hiredAt(new Date())
                .build();
        Employee expectedEmployee = employeeService.save(employee);
        int employeeId = expectedEmployee.getId();
        mockMvc.perform(put("/projects/{projectId}/employees/{employeeId}", projectId, employeeId))
                .andExpect(status().isOk());
        Employee actualEmployee = project.getEmployees().stream()
                .filter(employee1 -> employee1.getId() == expectedEmployee.getId()).findFirst().get();
        assertEquals(expectedEmployee, actualEmployee);
    }

    @Test
    void whenDeleteEmployeeThanReturnProjectThanContainThisEmployee() throws Exception {
        Project project = Project.builder()
                .name("Test Proj")
                .build();
        Employee employee = Employee.builder()
                .name("Ivan")
                .surname("Ivanov")
                .position(Position.DEVOPS)
                .experience(Experience.JUNIOR)
                .build();
        Project savedProj = projectService.save(project);

        Employee savedEmployee = employeeService.save(employee);
        mockMvc.perform(delete("/projects/{projectId}/employees/{employeeId}", savedProj.getId(), savedEmployee.getId()))
                .andExpect(status().isOk());
        assertEquals(savedProj, projectService.findById(savedProj.getId()));

    }

    @Test
    void whenDeleteProjectThanAfterAttemptToGetThisOneThrowProjectNotFoundException() throws Exception {
        Project project = Project.builder()
                .name("Test Proj")
                .build();
        Project savedProj = projectService.save(project);
        List<Project> expectedProjectList = projectService.findAll();
        mockMvc.perform(delete("/projects/{projectId}", savedProj.getId()))
                .andExpect(status().isOk());
        assertAll(
                () -> assertThrows(ProjectNotFoundException.class, () -> projectService.findById(savedProj.getId())),
                () -> assertEquals(expectedProjectList.size() - 1, projectService.findAll().size())
        );
    }

    @Test
    void whenAssignProjectManagerThanReturnStatus200AndAfterProjectContainsThisProjectManager() throws Exception {
        Project project = Project.builder()
                .name("Test Proj")
                .build();
        ProjectManager projectManager = ProjectManager.builder()
                .name("Anton")
                .surname("uzhva")
                .build();
        ProjectManager savedManager = projectManagerService.save(projectManager);
        Project savedProj = projectService.save(project);
        mockMvc.perform(put("/projects/{projectId}/manager/{managerId}", savedProj.getId(), savedManager.getId()))
                .andExpect(status().isOk());
        assertEquals(savedManager, projectService.findById(savedProj.getId()).getProjectManager());
    }

    @Test
    void whenDeleteProjManagerThanReturnStatus200AndAfterProjectDoesNotContainThisProjectManager() throws Exception {
        Project project = Project.builder()
                .name("Test Proj")
                .build();
        ProjectManager projectManager = ProjectManager.builder()
                .name("Anton")
                .surname("uzhva")
                .build();
        ProjectManager savedManager = projectManagerService.save(projectManager);
        Project savedProj = projectService.save(project);
        mockMvc.perform(put("/projects/{projectId}/manager/{managerId}", savedProj.getId(), savedManager.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/projects/{projectId}/manager/{managerId}", savedProj.getId(), savedManager.getId()))
                .andExpect(status().isOk());
        assertEquals(savedProj, projectService.findById(savedProj.getId()));
    }
}

