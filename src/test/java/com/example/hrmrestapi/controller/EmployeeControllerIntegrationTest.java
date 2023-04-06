package com.example.hrmrestapi.controller;

import com.example.hrmrestapi.dto.EmployeeDTO;
import com.example.hrmrestapi.model.Employee;
import com.example.hrmrestapi.model.Project;
import com.example.hrmrestapi.repository.EmployeeRepo;
import com.example.hrmrestapi.service.EmployeeService;
import com.example.hrmrestapi.util.EmployeeNotFoundException;
import com.example.hrmrestapi.util.Experience;
import com.example.hrmrestapi.util.NoAnyEmployeesException;
import com.example.hrmrestapi.util.Position;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional()
class EmployeeControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ObjectMapper objectMapper;

    Employee employee1;
    Employee employee2;
    List<Employee> employees;

    @Autowired
    EmployeeRepo employeeRepo;



    @Sql("/test.sql")
    @BeforeEach
    void setUp() {
        employeeRepo.deleteAll();
        employee1 = Employee.builder()
                .name("Ivan")
                .surname("Ivanov")
                .experience(Experience.JUNIOR)
                .position(Position.QA)
                .build();
        employee2 = Employee.builder()
                .name("Petr")
                .surname("Petrenko")
                .experience(Experience.MIDDLE)
                .position(Position.DEVOPS)
                .build();
        employees = new ArrayList<>(Arrays.asList(employee1, employee2));
    }

    @Test
    void whenGetAllEmployeesThanReturnProperListEmployeesDTO() throws Exception {
        employeeService.save(employee1);
        employeeService.save(employee2);
        MvcResult mvcResult = mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        List<EmployeeDTO> actualList = objectMapper.readValue(response, new TypeReference<List<EmployeeDTO>>() {
        });
        List<EmployeeDTO> expectedList = employeeService.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
        assertEquals(expectedList, actualList);

    }

    @Test
    void whenGetEmployeeByIdThanReturnEmployee() throws Exception {
        Employee savedEmployee = employeeService.save(employee1);
        MvcResult mvcResult = mockMvc.perform(get("/employees/{id}", savedEmployee.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        EmployeeDTO actualEmployeeDTO = objectMapper.readValue(response, EmployeeDTO.class);
        assertEquals(modelMapper.map(savedEmployee, EmployeeDTO.class), actualEmployeeDTO);
    }

    @Test
    void whenGetEmployeeByIdAndThereAreNoEmployeeWithSuchIdThanReturnStatus404AndThrowEmployeeNotFoundException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/{id}", 10000)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findById(10000));
    }

    @Test
    void whenCreateEmployeeWithProperFieldsThanReturnStatus201() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .name("Ivan")
                .surname("Ivanov")
                .experience(Experience.JUNIOR)
                .position(Position.QA)
                .build();
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenCreateEmployeeWithIncorrectFieldThanReturn404Status() throws Exception {
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .name("Iv")
                .surname("I")
                .experience(Experience.JUNIOR)
                .position(Position.QA)
                .build();
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdatePositionToProperValueThanReturnStatus200() throws Exception {
        Employee savedEmployee = employeeService.save(employee1);
        Map<String, String> request = new HashMap<>();
        request.put("position", "qa");
        mockMvc.perform(put("/employees/{id}/position", savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        assertEquals(employeeService.findById(savedEmployee.getId()).getPosition(), Position.QA);
    }

    @Test
    void whenUpdatePositionToInvalidValueThanReturnStatus404() throws Exception {
        Employee savedEmployee = employeeService.save(employee1);
        Map<String, String> request = new HashMap<>();
        request.put("position", "dummy");
        mockMvc.perform(put("/employees/{id}/position", savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateExperienceToProperValueThanReturnStatus200() throws Exception {
        Employee savedEmployee = employeeService.save(employee1);
        Map<String, String> request = new HashMap<>();
        request.put("experience", "junior");
        mockMvc.perform(put("/employees/{id}/experience", savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        assertEquals(employeeService.findById(savedEmployee.getId()).getExperience(), Experience.JUNIOR);
    }

    @Test
    void whenUpdateExperienceToInvalidValueThanReturnStatus404() throws Exception {
        Employee savedEmployee = employeeService.save(employee1);
        Map<String, String> request = new HashMap<>();
        request.put("experience", "dummy");
        mockMvc.perform(put("/employees/{id}/experience", savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenTryGetAllEmployeesButThereAreNoEmployeesThanReturnStatus400AndThrowNoEmployeeException() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees"))
                .andExpect(status().isNotFound())
                .andReturn();
        assertThrows(NoAnyEmployeesException.class, employeeService::findAll);
    }

    @Test
    void whenDeleteEmployeeThanReturnStatus200AndIfTryToGetThisEmployeeThrowEmployeeNotFoundException() throws Exception {
        Employee savedEmployee = employeeService.save(employee1);

        mockMvc.perform(delete("/employees/{id}", savedEmployee.getId()))
                .andExpect(status().isOk());
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.findById(savedEmployee.getId()));
    }

}