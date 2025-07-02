package com.pge.booklyzer.application.dto.book.output;

import com.pge.booklyzer.application.dto.author.output.AuthorOutputDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookFullOutputDTO extends BookBaseDTO {
  private AuthorOutputDTO author;
}
