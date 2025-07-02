package com.pge.booklyzer.application.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pge.booklyzer.application.dto.user.input.UserInputDTO;
import com.pge.booklyzer.application.dto.user.output.UserFullOutputDTO;
import com.pge.booklyzer.application.dto.user.output.UserOutputDTO;
import com.pge.booklyzer.domain.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import com.pge.booklyzer.shared.exceptions.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final UUID userId = UUID.randomUUID();
  private final String userName = "João Silva";
  private final String userEmail = "joao@example.com";

  @BeforeEach
  void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
    mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  void findAll_ShouldReturnPageOfUsers() throws Exception {
    UserOutputDTO outputDTO = new UserOutputDTO();
    outputDTO.setId(userId);
    outputDTO.setName(userName);
    outputDTO.setEmail(userEmail);

    Page<UserOutputDTO> page = new PageImpl<>(
            Collections.singletonList(outputDTO),
            PageRequest.of(0, 10, Sort.by("name").ascending()),
            1
    );

    when(userService.findAll(any(Pageable.class))).thenReturn(page);

    mockMvc.perform(get("/users")
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "name,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(userId.toString()))
            .andExpect(jsonPath("$.content[0].name").value(userName));

    verify(userService).findAll(any(Pageable.class));
  }

  @Test
  void findById_ShouldReturnUserWhenExists() throws Exception {
    UserFullOutputDTO fullOutputDTO = new UserFullOutputDTO();
    fullOutputDTO.setId(userId);
    fullOutputDTO.setName(userName);
    fullOutputDTO.setEmail(userEmail);

    when(userService.findById(userId)).thenReturn(fullOutputDTO);

    mockMvc.perform(get("/users/{id}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(userName))
            .andExpect(jsonPath("$.email").value(userEmail));

    verify(userService).findById(userId);
  }

  @Test
  void findById_ShouldReturn404WhenNotFound() throws Exception {
    when(userService.findById(userId)).thenThrow(new NotFoundException("Usuário não encontrado"));

    mockMvc.perform(get("/users/{id}", userId))
            .andExpect(status().isNotFound());

    verify(userService).findById(userId);
  }

  @Test
  void save_ShouldCreateUserAndReturn201() throws Exception {
    UserInputDTO inputDTO = new UserInputDTO(userName, "111", userEmail, "password");
    UserFullOutputDTO outputDTO = new UserFullOutputDTO();
    outputDTO.setId(userId);
    outputDTO.setName(userName);
    outputDTO.setEmail(userEmail);


    when(userService.save(any(UserInputDTO.class))).thenReturn(outputDTO);

    mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.name").value(userName));
  }

  @Test
  void save_ShouldReturn400WhenInvalidInput() throws Exception {
    UserInputDTO invalidDTO = new UserInputDTO("", "", "", "");

    mockMvc.perform(post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
            .andExpect(status().isBadRequest());
  }

  @Test
  void update_ShouldUpdateUserAndReturn200() throws Exception {
    UserInputDTO inputDTO = new UserInputDTO(userName, "11", userEmail, "password");
    UserFullOutputDTO outputDTO = new UserFullOutputDTO();
    outputDTO.setId(userId);
    outputDTO.setName(userName);
    outputDTO.setEmail(userEmail);

    when(userService.update(eq(userId), any(UserInputDTO.class))).thenReturn(outputDTO);

    mockMvc.perform(put("/users/{id}", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(userName));
  }

  @Test
  void update_ShouldReturn404WhenNotFound() throws Exception {
    UserInputDTO inputDTO = new UserInputDTO(userName, "11", userEmail, "password");

    when(userService.update(eq(userId), any(UserInputDTO.class)))
            .thenThrow(new NotFoundException("Usuário não encontrado"));

    mockMvc.perform(put("/users/{id}", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isNotFound());
  }
}