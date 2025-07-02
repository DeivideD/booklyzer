package com.pge.booklyzer.application.dto.bookloan.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class BookLoanFullOutputDTO extends BookLoanBaseDTO{
  @JsonProperty("userId")
  private UUID userId;

  @JsonProperty("bookId")
  private UUID bookId;
}
