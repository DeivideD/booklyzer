package com.pge.booklyzer.application.dto.bookloan.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pge.booklyzer.shared.annotation.DateBetweenNowAnd;
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
public class BookLoanInputDTO {
  @NotNull
  private UUID userId;
  @NotNull
  private UUID bookId;
  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @DateBetweenNowAnd(days = 14, message = "A data de retorno deve ser maior que o dia de hoje e menor que o prazo de 14 dias")
  private LocalDate expectedReturnDate;

}
