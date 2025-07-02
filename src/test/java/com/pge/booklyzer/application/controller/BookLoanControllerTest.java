package com.pge.booklyzer.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pge.booklyzer.application.dto.book.output.BookOutputDTO;
import com.pge.booklyzer.application.dto.bookloan.input.BookLoanInputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanFullOutputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanOutputDTO;
import com.pge.booklyzer.domain.service.BookLoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import jakarta.validation.constraints.NotNull;
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

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class BookLoanControllerTest {

  @Mock
  private BookLoanService bookLoanService;

  @InjectMocks
  private BookLoanController bookLoanController;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final UUID loanId = UUID.randomUUID();
  private final UUID userId = UUID.randomUUID();
  private final UUID bookId = UUID.randomUUID();
  private final LocalDate expectedReturnDate = LocalDate.now().plusDays(4L);

  @BeforeEach
  void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
    mockMvc = MockMvcBuilders.standaloneSetup(bookLoanController)
            .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
  }

  @Test
  void loanBook_ShouldCreateLoanAndReturn201() throws Exception {
    BookLoanInputDTO inputDTO = new BookLoanInputDTO(
            bookId,
            userId,
            expectedReturnDate
    );

    BookLoanFullOutputDTO outputDTO = new BookLoanFullOutputDTO();
    outputDTO.setId(loanId);
    outputDTO.setBookId(bookId);
    outputDTO.setUserId(userId);
    outputDTO.setExpectedReturnDate(expectedReturnDate);

    when(bookLoanService.loanBook(any(BookLoanInputDTO.class))).thenReturn(outputDTO);

    mockMvc.perform(post("/loans")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(inputDTO)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.expectedReturnDate").value(expectedReturnDate.toString()));
  }

  @Test
  void returnBook_ShouldUpdateLoanAndReturn200() throws Exception {
    BookLoanFullOutputDTO outputDTO = new BookLoanFullOutputDTO();
    outputDTO.setReturnDate(LocalDate.now());
    outputDTO.setId(loanId);

    when(bookLoanService.returnBook(loanId)).thenReturn(outputDTO);

    mockMvc.perform(put("/loans/{loanId}/return-book", loanId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.returnDate").exists());

    verify(bookLoanService).returnBook(loanId);
  }

  @Test
  void returnBook_ShouldReturn404WhenLoanNotFound() throws Exception {
    when(bookLoanService.returnBook(loanId)).thenThrow(new NotFoundException("Empréstimo não encontrado"));

    mockMvc.perform(put("/loans/{loanId}/return-book", loanId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
  }

  @Test
  void getUserLoans_ShouldReturnPageOfLoans() throws Exception {
    BookLoanOutputDTO outputDTO = new BookLoanOutputDTO();
    outputDTO.setId(loanId);
    Pageable pageable = PageRequest.of(0, 10);
    Page<BookLoanOutputDTO> page = new PageImpl<>(
            new ArrayList<>(List.of(outputDTO)),
            pageable,
            1
    );
    when(bookLoanService.getUserLoans(any(Pageable.class), eq(userId))).thenReturn(page);

    mockMvc.perform(get("/loans/user/{userId}", userId)
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "loanDate,desc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(loanId.toString()));

    verify(bookLoanService).getUserLoans(any(Pageable.class), eq(userId));
  }

  @Test
  void getActiveLoans_ShouldReturnPageOfActiveLoans() throws Exception {
    BookLoanOutputDTO outputDTO = new BookLoanOutputDTO();
    outputDTO.setId(loanId);
    Pageable pageable = PageRequest.of(0, 10);
    Page<BookLoanOutputDTO> page = new PageImpl<>(
            new ArrayList<>(List.of(outputDTO)),
            pageable,
            1
    );
    when(bookLoanService.getActiveLoans(any(Pageable.class))).thenReturn(page);

    mockMvc.perform(get("/loans/actives")
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "expectedReturnDate,asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(loanId.toString()));

    verify(bookLoanService).getActiveLoans(any(Pageable.class));
  }

  @Test
  void getUserLoans_ShouldUseDefaultPagination() throws Exception {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("loanDate").descending());
    Page<BookLoanOutputDTO> page = new PageImpl<>(
            new ArrayList<>(List.of()),
            pageable,
            1
    );

    when(bookLoanService.getUserLoans(any(Pageable.class), any(UUID.class))).thenReturn(page);

    mockMvc.perform(get("/loans/user/{userId}", userId))
            .andExpect(status().isOk());

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(bookLoanService).getUserLoans(pageableCaptor.capture(), any(UUID.class));

    Pageable pageableCaptorValue = pageableCaptor.getValue();
    assertEquals(0, pageableCaptorValue.getPageNumber());
    assertEquals(10, pageableCaptorValue.getPageSize());
    assertEquals(Sort.by("loanDate").descending(), pageable.getSort());
  }
}