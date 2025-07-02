package com.pge.booklyzer.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pge.booklyzer.application.controller.AuthController;
import com.pge.booklyzer.application.dto.auth.AuthenticationRequest;
import com.pge.booklyzer.application.dto.auth.AuthenticationResponse;
import com.pge.booklyzer.shared.exceptions.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private AuthService authService;

  @InjectMocks
  private AuthController authController;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  void authenticate_ShouldReturnTokenWhenCredentialsAreValid() throws Exception {
    AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");
    AuthenticationResponse response = new AuthenticationResponse("token123");

    when(authService.authenticate(request)).thenReturn(response);

    mockMvc.perform(post("/auth/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("token123"));
  }

  @Test
  void authenticate_ShouldReturnForbiddenWhenCredentialsAreInvalid() throws Exception {
    AuthenticationRequest request = new AuthenticationRequest("user@example.com", "wrongpass");

    when(authService.authenticate(request))
            .thenThrow(new BadCredentialsException("Credenciais inválidas"));

    mockMvc.perform(post("/auth/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Credenciais inválidas"));
  }
}