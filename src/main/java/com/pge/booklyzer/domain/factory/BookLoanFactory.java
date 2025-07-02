package com.pge.booklyzer.domain.factory;

import com.pge.booklyzer.domain.enuns.LoanStatus;
import com.pge.booklyzer.domain.model.Book;
import com.pge.booklyzer.domain.model.BookLoan;
import com.pge.booklyzer.domain.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookLoanFactory {

  public static BookLoan create() {
    return new BookLoan();
  }

  public static BookLoan create(Book book, User user) {
    BookLoan bookLoan = create();
    bookLoan.setBook(book);
    bookLoan.setUser(user);
    return bookLoan;
  }

  public static BookLoan create(Book book, User user, LocalDate loanDate) {
    BookLoan bookLoan = create(book, user);
    bookLoan.setLoanDate(loanDate);
    return bookLoan;
  }

  public static BookLoan create(Book book, User user, LocalDate loanDate, LoanStatus status) {
    BookLoan bookLoan = create(book, user, loanDate);
    bookLoan.setStatus(status);
    return bookLoan;
  }

  public static BookLoan create(Book book, User user, LocalDate loanDate, LoanStatus status, LocalDate dueDate) {
    BookLoan bookLoan = create(book, user, loanDate, status);
    bookLoan.setDueDate(dueDate);
    return bookLoan;
  }

  public static BookLoan create(Book book, User user, LocalDate loanDate, LoanStatus status, LocalDate dueDate, LocalDate expectedReturnDate) {
    BookLoan bookLoan = create(book, user, loanDate, status, dueDate);
    bookLoan.setExpectedReturnDate(expectedReturnDate);
    return bookLoan;
  }

}
