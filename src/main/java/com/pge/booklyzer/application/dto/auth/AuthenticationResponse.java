package com.pge.booklyzer.application.dto.auth;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {
  private String token;
}

