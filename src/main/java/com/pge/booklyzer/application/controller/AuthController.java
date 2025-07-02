package com.pge.booklyzer.application.controller;

import com.pge.booklyzer.application.dto.auth.AuthenticationRequest;
import com.pge.booklyzer.application.dto.auth.AuthenticationResponse;
import com.pge.booklyzer.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Autentique para acessar o sistema")
public class AuthController {

  private final AuthService authService;

  @Operation(
          summary = "Autentica o usuário no sistema",
          description = "Busca um usuário pelo email e compara se a senha está correta"
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Retorna um tocken"),
          @ApiResponse(responseCode = "403", description = "Em caso de não localizar o usuário ou senha incorreta"),
  })
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
          @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(authService.authenticate(request));
  }
}
