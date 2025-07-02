package com.pge.booklyzer.application.dto.author.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorInputDTO {
  @NotBlank(message = "Nome não pode ser vazio")
  @NotNull(message = "Nome não pode ser nulo")
  private String name;
}
