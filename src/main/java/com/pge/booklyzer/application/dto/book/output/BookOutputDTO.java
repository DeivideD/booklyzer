package com.pge.booklyzer.application.dto.book.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookOutputDTO extends BookBaseDTO {
  private String authorName;
}
