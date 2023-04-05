package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.EmployeeDTO;
import com.example.hrmrestapi.dto.ProjectDTO;
import com.example.hrmrestapi.dto.ProjectManagerDTO;
import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.model.ProjectManager;
import com.example.hrmrestapi.service.EmployeeService;
import com.example.hrmrestapi.service.ProjectService;
import com.example.hrmrestapi.util.Experience;
import com.example.hrmrestapi.util.Position;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class ProjectControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProjectService projectService;

    @MockBean
    EmployeeService employeeService;

    ProjectDTO projectDTO1;
    ProjectDTO projectDTO2;
    Project project1;
    Project project2;
    List<ProjectDTO> projectDTOList;
    List<Project> projectList;


    @BeforeEach
    void setUp() {
        project1 = new Project(1, "First Proj", new Date(), new ProjectManager(), new ArrayList<>());
        project2 = new Project(2, "Second Proj", new Date(), new ProjectManager(), new ArrayList<>());
        projectList = new ArrayList<>(Arrays.asList(
                project1,
                project2));
        projectDTO1 = new ProjectDTO(1, "First Proj", new Date(), new ProjectManagerDTO(), new ArrayList<>());
        projectDTO2 = new ProjectDTO(2, "Second Proj", new Date(), new ProjectManagerDTO(), new ArrayList<>());
        projectDTOList = new ArrayList<>(List.of(projectDTO1,
                projectDTO2));


    }

    @Test
    void whenGetAllProjectsThanReturnAllProjects() throws Exception {

        when(projectService.findAll())
                .thenReturn(projectList);

        MvcResult mvcResult = mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Second Proj", "First Proj")))
                .andReturn();
        //  String jsonResponse = mvcResult.getResponse().getContentAsString();
    }

    @Test
    void whenGetProjectByIdThanReturnExactlyThisOne() throws Exception {
        when(projectService.save(project1)).thenReturn(project1);
        when(projectService.findById(project1.getId())).thenReturn(project1);

        mockMvc.perform(get("/projects/{id}", project1.getId()))
                .andExpect(jsonPath("$.id", is(project1.getId())))
                .andExpect(jsonPath("$.name", is(project1.getName())));

    }

    @Test
    void whenCreateNewValidProjectThanReturnStatus201() throws Exception {
        when(projectService.save(project1)).thenReturn(project1);
        mockMvc.perform(post("/projects")
                        .content(objectMapper.writeValueAsString(projectDTO1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(projectService, times(1)).save(project1);
    }

    @Test
    void whenAssignValidEmployeeThanReturnStatus200() throws Exception {
        Employee employee = new Employee(2, "Anton", "Uzhva", Position.DEVELOPER,
                Experience.JUNIOR, new Date(), new ArrayList<>());
        when(projectService.findById(project1.getId())).thenReturn(project1);
        when(employeeService.findById(employee.getId())).thenReturn(employee);
        mockMvc.perform(put("/projects/{projectId}/employees/{employeeId}", project1.getId(), employee.getId()))
                .andExpect(status().isOk());
        verify(projectService, times(1)).findById(project1.getId());
        verify(employeeService, times(1)).findById(employee.getId());
        verify(projectService, times(1)).save(any());


    }

    @Test
    void whenDeleteEmployeeFromProjectReturnStatus200() throws Exception {
        Employee employee = new Employee(2, "Anton", "Uzhva", Position.DEVELOPER,
                Experience.JUNIOR, new Date(), new ArrayList<>());
        when(projectService.findById(project1.getId())).thenReturn(project1);
        when(employeeService.findById(employee.getId())).thenReturn(employee);
        mockMvc.perform(delete("/projects/{projectId}/employees/{employeeId}", project1.getId(), employee.getId()))
                .andExpect(status().isOk());
        verify(projectService, times(1)).findById(project1.getId());
        verify(employeeService, times(1)).findById(employee.getId());
        verify(projectService, times(1)).save(any());
    }

    @Test
    void deleteProject() {

    }

    @Test
    void handleException() {
    }

    @Test
    void testHandleException() {
    }

    @Test
    void testHandleException1() {
    }

    @Test
    void testHandleException2() {
    }
}