package com.pge.booklyzer.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pge.booklyzer.application.dto.bookloan.input.BookLoanInputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanFullOutputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanOutputDTO;
import com.pge.booklyzer.application.mapper.BookLoanMapper;
import com.pge.booklyzer.domain.enuns.LoanStatus;
import com.pge.booklyzer.domain.factory.BookFactory;
import com.pge.booklyzer.domain.factory.UserFactory;
import com.pge.booklyzer.domain.model.Book;
import com.pge.booklyzer.domain.model.BookLoan;
import com.pge.booklyzer.domain.model.User;
import com.pge.booklyzer.domain.repository.BookLoanRepository;
import com.pge.booklyzer.domain.service.BookLoanDomainService;
import com.pge.booklyzer.domain.service.BookService;
import com.pge.booklyzer.domain.service.UserService;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class BookLoanServiceTest {

  @Mock
  private BookLoanRepository repository;

  @Mock
  private BookLoanMapper mapper;

  @Mock
  private BookLoanDomainService bookLoanDomainService;

  @Mock
  private BookService bookService;

  @Mock
  private UserService userService;

  @InjectMocks
  private BookLoanServiceImpl bookLoanService;

  private final UUID bookId = UUID.randomUUID();
  private final UUID userId = UUID.randomUUID();
  private final UUID loanId = UUID.randomUUID();
  private final LocalDate today = LocalDate.now();
  private final LocalDate futureDate = today.plusDays(7);

  @Test
  void loanBook_ShouldCreateLoanWhenValidInput() {
    BookLoanInputDTO inputDTO = new BookLoanInputDTO(userId, bookId, futureDate);
    Book book = BookFactory.create(bookId);
    User user = UserFactory.create(userId);
    BookLoan savedLoan = new BookLoan();
    BookLoanFullOutputDTO outputDTO = new BookLoanFullOutputDTO();

    when(bookService.findBookById(bookId)).thenReturn(Optional.of(book));
    when(userService.findUserById(userId)).thenReturn(Optional.of(user));
    when(repository.currentLoansCount(userId)).thenReturn(0L);
    when(bookLoanDomainService.createLoan(any(), any(), any(), any(), any(), any())).thenReturn(savedLoan);
    when(repository.save(savedLoan)).thenReturn(savedLoan);
    when(mapper.toFullOutputDTO(savedLoan)).thenReturn(outputDTO);

    BookLoanFullOutputDTO result = bookLoanService.loanBook(inputDTO);

    assertNotNull(result);
    verify(bookService).reduceAvailableQuantity(bookId, 1);
  }

  @Test
  void loanBook_ShouldThrowNotFoundExceptionWhenBookNotFound() {
    BookLoanInputDTO inputDTO = new BookLoanInputDTO(userId, bookId, futureDate);

    when(bookService.findBookById(bookId)).thenReturn(Optional.empty());

    lenient().when(userService.findUserById(any())).thenReturn(Optional.of(new User()));
    lenient().when(repository.currentLoansCount(any())).thenReturn(0L);

    assertThrows(NotFoundException.class, () -> bookLoanService.loanBook(inputDTO));
  }

  @Test
  void loanBook_ShouldThrowNotFoundExceptionWhenUserNotFound() {
    BookLoanInputDTO inputDTO = new BookLoanInputDTO(userId, bookId, futureDate);
    Book book = new Book();

    when(bookService.findBookById(bookId)).thenReturn(Optional.of(book));
    when(userService.findUserById(userId)).thenReturn(Optional.empty());

    lenient().when(repository.currentLoansCount(any())).thenReturn(0L);

    assertThrows(NotFoundException.class, () -> bookLoanService.loanBook(inputDTO));
  }

  @Test
  void returnBook_ShouldUpdateLoanStatus() {
    BookLoan activeLoan = new BookLoan();
    Book book = new Book();
    activeLoan.setBook(book);
    BookLoanFullOutputDTO outputDTO = new BookLoanFullOutputDTO();

    when(repository.findByIdAndStatusActive(loanId)).thenReturn(Optional.of(activeLoan));
    when(mapper.toFullOutputDTO(activeLoan)).thenReturn(outputDTO);

    BookLoanFullOutputDTO result = bookLoanService.returnBook(loanId);

    assertNotNull(result);
    assertEquals(LoanStatus.RETURNED, activeLoan.getStatus());
    assertNotNull(activeLoan.getReturnDate());
    verify(bookService).increaseAvailableQuantity(book.getId(), 1);
  }

  @Test
  void returnBook_ShouldThrowNotFoundExceptionWhenLoanNotFound() {
    when(repository.findByIdAndStatusActive(loanId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> bookLoanService.returnBook(loanId));
  }

  @Test
  void getUserLoans_ShouldReturnPageOfLoans() {
    Pageable pageable = mock(Pageable.class);
    Page<BookLoan> loanPage = new PageImpl<>(List.of(new BookLoan()));

    when(repository.findByUserId(pageable, userId)).thenReturn(loanPage);

    Page<BookLoanOutputDTO> result = bookLoanService.getUserLoans(pageable, userId);

    assertNotNull(result);
    verify(repository).findByUserId(pageable, userId);
  }

  @Test
  void getActiveLoans_ShouldReturnPageOfActiveLoans() {
    Pageable pageable = mock(Pageable.class);
    Page<BookLoan> loanPage = new PageImpl<>(List.of(new BookLoan()));

    when(repository.findByStatus(pageable, LoanStatus.ACTIVE)).thenReturn(loanPage);

    Page<BookLoanOutputDTO> result = bookLoanService.getActiveLoans(pageable);

    assertNotNull(result);
    verify(repository).findByStatus(pageable, LoanStatus.ACTIVE);
  }
}