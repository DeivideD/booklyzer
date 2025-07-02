package com.pge.booklyzer.application.dto.bookloan.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookLoanBaseDTO {
  private UUID id;
  private String userName;
  private String bookTitle;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate expectedReturnDate;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate loanDate;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dueDate;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate returnDate;
  private String status;
}
