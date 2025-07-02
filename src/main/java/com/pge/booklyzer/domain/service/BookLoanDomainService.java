package com.pge.booklyzer.domain.service;

import com.pge.booklyzer.domain.enuns.LoanStatus;
import com.pge.booklyzer.domain.exception.DomainException;
import com.pge.booklyzer.domain.factory.BookLoanFactory;
import com.pge.booklyzer.domain.model.Book;
import com.pge.booklyzer.domain.model.BookLoan;
import com.pge.booklyzer.domain.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class BookLoanDomainService {
  private static final int MAX_LOANS_PER_USER = 5;
  private static final int DEFAULT_LOAN_DAYS = 14;

  public boolean isBookAvailable(Book book) {
    if (book == null) {
      throw new IllegalArgumentException("Livro não pode ser vazio");
    }
    return !(book.getAvailableQuantity() == 0);
  }

  public void validateUserLoanLimit(User user, Long currentLoansCount) {
    if (user == null) {
      throw new IllegalArgumentException("Livro não pode ser vazio");
    }

    if (currentLoansCount >= MAX_LOANS_PER_USER) {
      throw new DomainException("Usuário atingiu o limite máximo de " + MAX_LOANS_PER_USER + " empréstimos simultâneos");
    }
  }

  public LocalDate calculateDueDate(LocalDate loanDate) {
    return loanDate.plusDays(DEFAULT_LOAN_DAYS);
  }

  public BookLoan createLoan(Book book, User user,
                                LocalDate loanDate, LoanStatus status,
                                LocalDate expectedReturnDate, Long currentLoansCount) {
    validateUserLoanLimit(user, currentLoansCount);

    if (!isBookAvailable(book)) {
      throw new DomainException("O livro não está disponível para empréstimo");
    }

    return BookLoanFactory.create(
            book, user, loanDate, status,
            calculateDueDate(loanDate), expectedReturnDate);
  }

}
