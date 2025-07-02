package com.pge.booklyzer.application.service;

import com.pge.booklyzer.application.dto.bookloan.input.BookLoanInputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanFullOutputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanOutputDTO;
import com.pge.booklyzer.application.mapper.BookLoanMapper;
import com.pge.booklyzer.domain.enuns.LoanStatus;
import com.pge.booklyzer.domain.model.Book;
import com.pge.booklyzer.domain.model.BookLoan;
import com.pge.booklyzer.domain.model.User;
import com.pge.booklyzer.domain.repository.BookLoanRepository;
import com.pge.booklyzer.domain.service.BookLoanDomainService;
import com.pge.booklyzer.domain.service.BookLoanService;
import com.pge.booklyzer.domain.service.BookService;
import com.pge.booklyzer.domain.service.UserService;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {
  private final BookLoanRepository repository;
  private final BookLoanMapper mapper;
  private final BookLoanDomainService bookLoanDomainService;
  private final BookService bookService;
  private final UserService userService;

  @Override
  @Transactional
  public BookLoanFullOutputDTO loanBook(BookLoanInputDTO loanDTO) {
    Book book = bookService.findBookById(loanDTO.getBookId()).orElseThrow(() -> new NotFoundException("Não foi possível localizar o livro"));
    User user = userService.findUserById(loanDTO.getUserId()).orElseThrow(() -> new NotFoundException("Não foi possível localizar o Usuário"));
    Long currentLoansCount = repository.currentLoansCount(user.getId());

    BookLoan bookLoan = create(loanDTO, book, user, currentLoansCount);

    bookService.reduceAvailableQuantity(book.getId(), 1);
    return mapper.toFullOutputDTO(bookLoan);
  }

  @Override
  @Transactional
  public BookLoanFullOutputDTO returnBook(UUID loanId) {
    BookLoan bookLoan = repository.findByIdAndStatusActive(loanId).orElseThrow(() -> new NotFoundException("Não foi possível localizar o emprestimo ativo"));
    bookLoan.setReturnDate(LocalDate.now());
    bookLoan.setStatus(LoanStatus.RETURNED);
    bookService.increaseAvailableQuantity(bookLoan.getBook().getId(), 1);
    return mapper.toFullOutputDTO(bookLoan);
  }

  @Override
  public Page<BookLoanOutputDTO> getUserLoans(Pageable pageable, UUID userId) {
    return repository.findByUserId(pageable, userId).map(mapper::toOutputDTO);
  }

  @Override
  public Page<BookLoanOutputDTO> getActiveLoans(Pageable pageable) {
    return repository.findByStatus(pageable, LoanStatus.ACTIVE)
            .map(mapper::toOutputDTO);
  }

  private BookLoan create(BookLoanInputDTO loanDTO, Book book, User user, Long currentLoansCount) {
    BookLoan bookLoan = bookLoanDomainService
            .createLoan(book, user, LocalDate.now(), LoanStatus.ACTIVE, loanDTO.getExpectedReturnDate(), currentLoansCount);
    return repository.save(bookLoan);
  }
}
