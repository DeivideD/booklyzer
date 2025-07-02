package com.pge.booklyzer.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AuthenticationRequest {
  private String email;
  private String password;
}
