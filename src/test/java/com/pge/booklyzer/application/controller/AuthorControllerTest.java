package com.pge.booklyzer.application.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.pge.booklyzer.application.dto.author.input.AuthorInputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorFullOutputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorOutputDTO;
import com.pge.booklyzer.domain.filter.AuthorFilter;
import com.pge.booklyzer.domain.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
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
class AuthorControllerTest {

  @Mock
  private AuthorService authorService;

  @InjectMocks
  private AuthorController authorController;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final UUID authorId = UUID.randomUUID();
  private final String authorName = "Machado de Assis";


  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authorController)
            .setMessageConverters(new MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
  }


  @Test
  void findAll_ShouldReturnPageOfAuthors() throws Exception {
    AuthorOutputDTO outputDTO = new AuthorOutputDTO();
    outputDTO.setId(authorId);
    outputDTO.setName(authorName);

    Pageable pageable = PageRequest.of(0, 10);
    Page<AuthorOutputDTO> page = new PageImpl<>(
            new ArrayList<>(List.of(outputDTO)),
            pageable,
            1
    );

    when(authorService.findAll(any(Pageable.class), any(AuthorFilter.class)))
            .thenReturn(page);

    mockMvc.perform(get("/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("name", authorName))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(authorId.toString()))
            .andExpect(jsonPath("$.content[0].name").value(authorName));

    verify(authorService).findAll(any(Pageable.class), any(AuthorFilter.class));
  }

  @Test
  void findById_ShouldReturnAuthorWhenExists() throws Exception {
    AuthorFullOutputDTO fullOutputDTO = new AuthorFullOutputDTO();
    fullOutputDTO.setId(authorId);
    fullOutputDTO.setName(authorName);

    when(authorService.findById(authorId)).thenReturn(fullOutputDTO);

    mockMvc.perform(get("/authors/{id}", authorId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(authorName));

    verify(authorService).findById(authorId);
  }

  @Test
  void save_ShouldCreateAuthorAndReturn201() throws Exception {
    AuthorInputDTO inputDTO = new AuthorInputDTO(authorName);
    AuthorFullOutputDTO fullOutputDTO = new AuthorFullOutputDTO();
    fullOutputDTO.setId(authorId);
    fullOutputDTO.setName(authorName);

    when(authorService.save(any(AuthorInputDTO.class))).thenReturn(fullOutputDTO);

    mockMvc.perform(post("/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.name").value(authorName));

    verify(authorService).save(any(AuthorInputDTO.class));
  }

  @Test
  void update_ShouldUpdateAuthorAndReturn200() throws Exception {
    AuthorInputDTO inputDTO = new AuthorInputDTO(authorName);
    AuthorFullOutputDTO fullOutputDTO = new AuthorFullOutputDTO();
    fullOutputDTO.setId(authorId);
    fullOutputDTO.setName(authorName);

    when(authorService.update(eq(authorId), any(AuthorInputDTO.class))).thenReturn(fullOutputDTO);

    mockMvc.perform(put("/authors/{id}", authorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(authorName));

    verify(authorService).update(eq(authorId), any(AuthorInputDTO.class));
  }

  @Test
  void save_ShouldReturn400WhenInvalidInput() throws Exception {
    AuthorInputDTO invalidDTO = new AuthorInputDTO("");

    mockMvc.perform(post("/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
            .andExpect(status().isBadRequest());
  }

  @Test
  void update_ShouldReturn404WhenAuthorNotFound() throws Exception {
    AuthorInputDTO inputDTO = new AuthorInputDTO(authorName);

    when(authorService.update(eq(authorId), any(AuthorInputDTO.class)))
            .thenThrow(new NotFoundException("Autor não encontrado"));

    mockMvc.perform(put("/authors/{id}", authorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isNotFound());
  }
}