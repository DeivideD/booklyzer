package com.pge.booklyzer.domain.service;

import com.pge.booklyzer.application.dto.bookloan.input.BookLoanInputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanFullOutputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanOutputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BookLoanService {
  BookLoanFullOutputDTO loanBook(BookLoanInputDTO loanDTO);
  BookLoanFullOutputDTO returnBook(UUID loanId);
  Page<BookLoanOutputDTO> getUserLoans(Pageable pageable, UUID userId);
  Page<BookLoanOutputDTO> getActiveLoans(Pageable pageable);
}
