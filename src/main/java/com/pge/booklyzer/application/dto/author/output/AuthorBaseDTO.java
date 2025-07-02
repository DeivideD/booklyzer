package com.pge.booklyzer.application.dto.author.output;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class AuthorBaseDTO {
  private UUID id;
  private String name;

}
