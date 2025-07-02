package com.pge.booklyzer.application.controller.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pge.booklyzer.application.dto.auth.AuthenticationRequest;
import com.pge.booklyzer.application.dto.auth.AuthenticationResponse;
import com.pge.booklyzer.application.dto.bookloan.input.BookLoanInputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanFullOutputDTO;
import com.pge.booklyzer.domain.enuns.LoanStatus;
import com.pge.booklyzer.domain.model.Author;
import com.pge.booklyzer.domain.model.Book;
import com.pge.booklyzer.domain.model.BookLoan;
import com.pge.booklyzer.domain.model.User;
import com.pge.booklyzer.domain.repository.AuthorRepository;
import com.pge.booklyzer.domain.repository.BookLoanRepository;
import com.pge.booklyzer.domain.repository.BookRepository;
import com.pge.booklyzer.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BookLoanControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private AuthorRepository authorRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private BookLoanRepository bookLoanRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private UUID bookId;
  private UUID userId;
  private String jwtToken;
  private final String expectedReturnDate = LocalDate.now().plusDays(13).toString();

  @BeforeEach
  void setUp() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    Author author = new Author();
    author.setName("John Doe");
    authorRepository.save(author);


    Book book = new Book();
    book.setTitle("Domain-Driven Design");
    book.setAvailableQuantity(5);
    book.setIsbn("9780451524935");
    book.setAuthor(author);
    book = bookRepository.save(book);
    bookId = book.getId();

    User user = new User();
    user.setName("Test User");
    String userEmail = "testuser@example.com";
    user.setEmail(userEmail);
    String userPassword = "password123";
    user.setPassword(passwordEncoder.encode(userPassword));
    user = userRepository.save(user);
    userId = user.getId();

    AuthenticationRequest authRequest = new AuthenticationRequest(userEmail, userPassword);

    MvcResult authResult = mockMvc.perform(post("/api/auth/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andReturn();

    AuthenticationResponse authResponse = objectMapper.readValue(
            authResult.getResponse().getContentAsString(),
            AuthenticationResponse.class
    );

    jwtToken = authResponse.getToken();
  }

  @Test
  void loanBook_ShouldCreateLoanWithValidToken() throws Exception {
    mockMvc.perform(post("/api/loans")
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "bookId": "%s",
                        "userId": "%s",
                        "expectedReturnDate": "%s"
                    }
                    """.formatted(bookId, userId, expectedReturnDate)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.id").exists());
  }


  @Test
  void loanBook_ShouldReturn403WithoutToken() throws Exception {
    mockMvc.perform(post("/loans")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                    {
                        "bookId": "%s",
                        "userId": "%s",
                        "expectedReturnDate": "%s"
                    }
                    """.formatted(bookId, userId, expectedReturnDate)))
            .andExpect(status().isForbidden());
  }

  @Test
  void loanBook_ShouldDecreaseAvailableQuantity() throws Exception {
    MvcResult result = mockMvc.perform(post("/api/loans")
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                        "bookId": "%s",
                        "userId": "%s",
                        "expectedReturnDate": "%s"
                    }
                    """.formatted(bookId, userId, expectedReturnDate))).andReturn();
    BookLoanFullOutputDTO bookLoanFullOutputDTO = objectMapper.readValue(result.getResponse().getContentAsString(), BookLoanFullOutputDTO.class);

    BookLoan bookLoan = bookLoanRepository.findById(bookLoanFullOutputDTO.getId()).orElse(null);
    assertNotNull(bookLoan);
    assertEquals(4, bookRepository.findAvailableQuantityById(bookId));
  }

  @Test
  void loanBook_ReturnBookAndIncreaseAvailableQuantity() throws Exception {
    MvcResult result = mockMvc.perform(post("/api/loans")
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {
                        "bookId": "%s",
                        "userId": "%s",
                        "expectedReturnDate": "%s"
                    }
                    """.formatted(bookId, userId, expectedReturnDate))).andReturn();
    BookLoanFullOutputDTO bookLoanFullOutputDTO = objectMapper.readValue(result.getResponse().getContentAsString(), BookLoanFullOutputDTO.class);
    assertEquals(4, bookRepository.findAvailableQuantityById(bookId));

    MvcResult returnResult = mockMvc.perform(put("/api/loans/" + bookLoanFullOutputDTO.getId() + "/return-book")
            .header("Authorization", "Bearer " + jwtToken)).andReturn();

    BookLoanFullOutputDTO bookLoanUpdateFullOutputDTO = objectMapper.readValue(returnResult.getResponse().getContentAsString(), BookLoanFullOutputDTO.class);

    assertNotNull(bookLoanUpdateFullOutputDTO);
    assertEquals(LoanStatus.RETURNED.name(), bookLoanUpdateFullOutputDTO.getStatus());
    assertNotNull(bookLoanUpdateFullOutputDTO.getReturnDate());
    assertEquals(5, bookRepository.findAvailableQuantityById(bookId));
  }


}