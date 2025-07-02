package com.pge.booklyzer.application.dto.user.input;

import jakarta.validation.constraints.Email;
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
public class UserInputDTO {
  @NotBlank(message = "Nome não pode ser vazio")
  @NotNull(message = "Nome não pode ser nulo")
  private String name;
  private String registration;
  @NotBlank(message = "Email não pode ser vazio")
  @NotNull(message = "Email não pode ser nulo")
  @Email(message = "Email com formato inválido")
  private String email;
  @NotBlank(message = "Senha não pode ser vazia")
  @NotNull(message = "Senha não pode ser nula")
  private String password;
}
