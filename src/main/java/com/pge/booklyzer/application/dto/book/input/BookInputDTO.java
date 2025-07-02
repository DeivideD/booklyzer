package com.pge.booklyzer.application.dto.book.input;

import com.pge.booklyzer.shared.annotation.ISBN;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookInputDTO {
  @NotBlank(message = "Titulo não pode ser vazio")
  @NotNull(message = "Titulo não pode ser nulo")
  private String title;
  @ISBN(message = "ISBN é invãlido")
  @NotNull(message = "ISBN é obrigatório")
  private String isbn;
  @NotNull
  @Min(0)
  private Integer availableQuantity;
  @NotNull(message = "Autor não pode ser nulo")
  private UUID authorId;
}
