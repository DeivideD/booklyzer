package com.pge.booklyzer.application.dto.book.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookBaseDTO {
  private UUID id;
  private String title;
  private String isbn;
  private Integer availableQuantity;
}
