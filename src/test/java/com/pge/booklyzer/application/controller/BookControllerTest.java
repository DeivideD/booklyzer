package com.pge.booklyzer.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.pge.booklyzer.application.dto.author.output.AuthorOutputDTO;
import com.pge.booklyzer.application.dto.book.input.BookInputDTO;
import com.pge.booklyzer.application.dto.book.output.BookFullOutputDTO;
import com.pge.booklyzer.application.dto.book.output.BookOutputDTO;
import com.pge.booklyzer.domain.factory.AuthorFactory;
import com.pge.booklyzer.domain.filter.BookFilter;
import com.pge.booklyzer.domain.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class BookControllerTest {

  @Mock
  private BookService bookService;

  @InjectMocks
  private BookController bookController;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final UUID bookId = UUID.randomUUID();
  private final UUID authorId = UUID.randomUUID();
  private final String bookTitle = "Dom Casmurro";
  private final String isbn = "978-3-16-148410-0";

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(bookController)
            .setMessageConverters(new MappingJackson2HttpMessageConverter())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
  }

  @Test
  void findAll_ShouldReturnPageOfBooks() throws Exception {
    BookOutputDTO outputDTO = new BookOutputDTO();
    outputDTO.setId(bookId);
    outputDTO.setTitle(bookTitle);
    outputDTO.setIsbn(isbn);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
    Page<BookOutputDTO> page = new PageImpl<>(
            new ArrayList<>(List.of(outputDTO)),
            pageable,
            1
    );

    when(bookService.findAll(any(Pageable.class), any(BookFilter.class)))
            .thenReturn(page);

    mockMvc.perform(get("/books")
                    .param("title", bookTitle)
                    .param("ISBN", isbn)
                    .param("authorId", authorId.toString())
                    .param("available", "true")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(bookId.toString()))
            .andExpect(jsonPath("$.content[0].title").value(bookTitle))
            .andExpect(jsonPath("$.content[0].isbn").value(isbn));

    verify(bookService).findAll(any(Pageable.class), any(BookFilter.class));
  }

  @Test
  void findById_ShouldReturnBookWhenExists() throws Exception {
    BookFullOutputDTO fullOutputDTO = new BookFullOutputDTO();
    fullOutputDTO.setId(bookId);
    fullOutputDTO.setTitle(bookTitle);
    fullOutputDTO.setIsbn(isbn);
    fullOutputDTO.setAuthor(new AuthorOutputDTO());

    when(bookService.findById(bookId)).thenReturn(fullOutputDTO);

    mockMvc.perform(get("/books/{id}", bookId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(bookTitle))
            .andExpect(jsonPath("$.isbn").value(isbn));

    verify(bookService).findById(bookId);
  }

  @Test
  void create_ShouldCreateBookAndReturn201() throws Exception {
    BookInputDTO inputDTO = new BookInputDTO(bookTitle, isbn, 5, authorId);
    BookFullOutputDTO fullOutputDTO = new BookFullOutputDTO();
    fullOutputDTO.setId(bookId);
    fullOutputDTO.setTitle(bookTitle);
    fullOutputDTO.setIsbn(isbn);

    when(bookService.save(any(BookInputDTO.class))).thenReturn(fullOutputDTO);

    mockMvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.title").value(bookTitle));

    verify(bookService).save(any(BookInputDTO.class));
  }

  @Test
  void update_ShouldUpdateBookAndReturn200() throws Exception {
    BookInputDTO inputDTO = new BookInputDTO(bookTitle, isbn, 3, authorId);
    BookFullOutputDTO fullOutputDTO = new BookFullOutputDTO();
    fullOutputDTO.setId(bookId);
    fullOutputDTO.setTitle(bookTitle);
    fullOutputDTO.setIsbn(isbn);

    when(bookService.update(eq(bookId), any(BookInputDTO.class))).thenReturn(fullOutputDTO);

    mockMvc.perform(put("/books/{id}", bookId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value(bookTitle));

    verify(bookService).update(eq(bookId), any(BookInputDTO.class));
  }

  @Test
  void create_ShouldReturn400WhenInvalidInput() throws Exception {
    BookInputDTO invalidDTO = new BookInputDTO("", "", -1, null);

    mockMvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
            .andExpect(status().isBadRequest());
  }

  @Test
  void update_ShouldReturn404WhenBookNotFound() throws Exception {
    BookInputDTO inputDTO = new BookInputDTO(bookTitle, isbn, 3, authorId);

    when(bookService.update(eq(bookId), any(BookInputDTO.class)))
            .thenThrow(new NotFoundException("Livro não encontrado"));

    mockMvc.perform(put("/books/{id}", bookId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isNotFound());
  }

  @Test
  void findAll_ShouldUseDefaultPagination() throws Exception {
    BookOutputDTO outputDTO = new BookOutputDTO();
    outputDTO.setId(bookId);
    outputDTO.setTitle(bookTitle);

    Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
    Page<BookOutputDTO> page = new PageImpl<>(
            new ArrayList<>(List.of(outputDTO)),
            pageable,
            1
    );

    when(bookService.findAll(any(Pageable.class), any(BookFilter.class)))
            .thenReturn(page);

    mockMvc.perform(get("/books")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(bookService).findAll(pageableCaptor.capture(), any(BookFilter.class));

    Pageable pageableCaptorValue = pageableCaptor.getValue();
    assertEquals(0, pageableCaptorValue.getPageNumber());
    assertEquals(10, pageableCaptorValue.getPageSize());
    assertEquals(Sort.by("title").ascending(), pageable.getSort());
  }
}