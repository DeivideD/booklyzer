package com.pge.booklyzer.domain.repository;

import com.pge.booklyzer.domain.enuns.LoanStatus;
import com.pge.booklyzer.domain.model.BookLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookLoanRepository extends JpaRepository<BookLoan, UUID> {
  Page<BookLoan> findByUserId(Pageable pageable, UUID userId);
  @Query("select bl from BookLoan bl " +
          "where bl.id = :id and bl.status = com.pge.booklyzer.domain.enuns.LoanStatus.ACTIVE and bl.returnDate is null")
  Optional<BookLoan> findByIdAndStatusActive(UUID id);
  Page<BookLoan> findByStatus(Pageable pageable, LoanStatus status);
  @Query("select count(bl) from BookLoan bl " +
          " where bl.user.id = :userId and bl.status = com.pge.booklyzer.domain.enuns.LoanStatus.ACTIVE" +
          " and bl.returnDate is null")
  Long currentLoansCount(UUID userId);
}
