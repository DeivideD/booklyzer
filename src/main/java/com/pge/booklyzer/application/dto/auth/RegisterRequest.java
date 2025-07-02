package com.pge.booklyzer.application.dto.auth;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {
  private String email;
  private String password;
}
