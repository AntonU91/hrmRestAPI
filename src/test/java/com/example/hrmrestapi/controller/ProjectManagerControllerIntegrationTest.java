package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.ProjectManagerDTO;
import com.example.hrmrestapi.model.ProjectManager;
import com.example.hrmrestapi.repository.ProjectManagerRepo;
import com.example.hrmrestapi.service.ProjectManagerService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional()
class ProjectManagerControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectManagerService projectManagerService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProjectManagerRepo projectManagerRepo;

    ProjectManager manager1;
    ProjectManager manager2;

    @BeforeEach
    @Sql("/test.sql")
    void setUp() {
        manager1 = ProjectManager.builder()
                .name("Ivan")
                .surname("Ivanov")
                .build();
        manager2 = ProjectManager.builder()
                .name("Anton")
                .surname("Antonov")
                .build();
        projectManagerRepo.deleteAll();
    }

    @Test
    void whenGetAllManagersThanReturnStatus200() throws Exception {
        ProjectManager projectManager1 = projectManagerService.save(manager1);
        ProjectManager projectManager2 = projectManagerService.save(manager2);
        List<ProjectManagerDTO> expectedList = projectManagerService.findAll().stream()
                .map(projectManager -> modelMapper.map(projectManager, ProjectManagerDTO.class))
                .collect(Collectors.toList());
        MvcResult mvcResult = mockMvc.perform(get("/managers"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        List<ProjectManagerDTO> actualList = objectMapper.readValue(response, new TypeReference<List<ProjectManagerDTO>>() {
        });
        assertEquals(expectedList, actualList);
    }

    @Test
    void whenGetManagerByIdThanReturnStatus200() throws Exception {
        ProjectManager expectedManager = projectManagerService.save(manager1);
        MvcResult mvcResult = mockMvc.perform(get("/managers/{id}", expectedManager.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        ProjectManagerDTO actualManager = objectMapper.readValue(response, ProjectManagerDTO.class);
        assertEquals(modelMapper.map(expectedManager, ProjectManagerDTO.class), actualManager);
    }
    @Test
    void whenCreateManagerWithProperFieldsThanReturnStatus201 () throws Exception {
        ProjectManagerDTO projectManagerDTO = ProjectManagerDTO.builder()
                .name("Ivan")
                .surname("Ivanov")
                .build();
        mockMvc.perform(post("/managers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectManagerDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}