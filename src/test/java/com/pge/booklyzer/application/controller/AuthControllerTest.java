package com.pge.booklyzer.application.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pge.booklyzer.application.dto.auth.AuthenticationRequest;
import com.pge.booklyzer.application.dto.auth.AuthenticationResponse;
import com.pge.booklyzer.application.dto.auth.RegisterRequest;
import com.pge.booklyzer.application.service.AuthService;
import com.pge.booklyzer.shared.exceptions.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private AuthService authService;

  @InjectMocks
  private AuthController authController;

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final String email = "user@example.com";
  private final String password = "password123";
  private final String token = "test.jwt.token";

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  void authenticate_ShouldReturnTokenWhenCredentialsAreValid() throws Exception {
    AuthenticationRequest request = new AuthenticationRequest(email, password);
    AuthenticationResponse response = new AuthenticationResponse(token);

    when(authService.authenticate(request)).thenReturn(response);

    mockMvc.perform(post("/auth/authenticate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value(token));

    verify(authService).authenticate(request);
  }

}