package com.pge.booklyzer.application.service;

import com.pge.booklyzer.application.dto.auth.AuthenticationRequest;
import com.pge.booklyzer.application.dto.auth.AuthenticationResponse;
import com.pge.booklyzer.application.dto.auth.RegisterRequest;
import com.pge.booklyzer.domain.model.User;
import com.pge.booklyzer.domain.repository.UserRepository;
import com.pge.booklyzer.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();

    userRepository.save(user);

    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow();

    var jwtToken = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
  }
}
