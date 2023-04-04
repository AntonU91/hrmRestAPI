package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.ProjectDTO;
import com.example.hrmrestapi.dto.ProjectManagerDTO;
import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.model.ProjectManager;
import com.example.hrmrestapi.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
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

    ProjectDTO projectDTO;
    List<ProjectDTO> projectDTOList;

    List<Project> projectList;

    @BeforeEach
    void setUp() {
        projectDTO = new ProjectDTO(1, "Second Proj", new Date(), new ProjectManagerDTO(), new ArrayList<>());
        projectDTOList = new ArrayList<>(List.of(projectDTO,
                new ProjectDTO(2, "Second Proj", new Date(), new ProjectManagerDTO(), new ArrayList<>())));
        projectList = new ArrayList<>(Arrays.asList(
                new Project(1, "Second Proj", new Date(), new ProjectManager(), new ArrayList<>()),
                new Project(2, "Second Proj", new Date(), new ProjectManager(), new ArrayList<>())));
    }

    @Test
    void whenGetAllProjectsThanReturnedAllProject() throws Exception {

        when(projectService.findAll())
                .thenReturn(projectList);

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
               // .andExpect(content().json(objectMapper.writeValueAsString(projectDTOList)));
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(content().json(objectMapper.writeValueAsString(projectDTOList)));
    }

    @Test
    void getProject() {
    }

    @Test
    void create() {
    }

    @Test
    void assignEmployee() {
    }

    @Test
    void deleteEmployee() {
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