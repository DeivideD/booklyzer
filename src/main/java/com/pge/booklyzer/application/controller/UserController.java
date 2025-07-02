package com.pge.booklyzer.application.controller;

import com.pge.booklyzer.application.dto.user.input.UserInputDTO;
import com.pge.booklyzer.application.dto.user.output.UserFullOutputDTO;
import com.pge.booklyzer.application.dto.user.output.UserOutputDTO;
import com.pge.booklyzer.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "API para gerenciamento de usuários")
public class UserController {

  private final UserService userService;

  @Operation(
          summary = "Listar todos os usuários",
          description = "Retorna uma lista paginada de todos os usuários cadastrados",
          parameters = {
                  @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
                  @Parameter(name = "size", description = "Quantidade de itens por página", example = "10"),
                  @Parameter(name = "sort", description = "Critérios de ordenação (formato: propriedade,asc|desc)", example = "name,asc")
          }
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso"),
          @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
  })
  @GetMapping
  public ResponseEntity<Page<UserOutputDTO>> findAll(Pageable pageable) {
    Page<UserOutputDTO> users = userService.findAll(pageable);
    return ResponseEntity.ok(users);
  }

  @Operation(
          summary = "Buscar usuário por ID",
          description = "Retorna um usuário específico com base no seu identificador único"
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Usuário encontrado e retornado"),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
          @ApiResponse(responseCode = "400", description = "Formato de UUID inválido")
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserFullOutputDTO> findById(@PathVariable UUID id) {
    UserFullOutputDTO user = userService.findById(id);
    return ResponseEntity.ok(user);
  }

  @Operation(
          summary = "Criar novo usuário",
          description = "Cadastra um novo usuário com os dados fornecidos"
  )
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
          @ApiResponse(responseCode = "409", description = "Usuário já existe")
  })
  @PostMapping
  public ResponseEntity<UserFullOutputDTO> save(
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Dados do usuário a ser criado",
                  required = true,
                  content = @Content(schema = @Schema(implementation = UserInputDTO.class))
          )
          @Valid @RequestBody UserInputDTO dto) {
    UserFullOutputDTO user = userService.save(dto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(user.getId())
            .toUri();
    return ResponseEntity.created(location).body(user);
  }


  @Operation(
          summary = "Atualizar usuário existente",
          description = "Atualiza os dados de um usuário existente identificado pelo seu ID"
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
          @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
          @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
  })
  @PutMapping("/{id}")
  public ResponseEntity<UserFullOutputDTO> update(
          @Parameter(description = "UUID do usuário a ser atualizado", example = "123e4567-e89b-12d3-a456-426614174000")
          @PathVariable UUID id,

          @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Dados atualizados do usuário",
                  required = true,
                  content = @Content(schema = @Schema(implementation = UserInputDTO.class))
          )
          @Valid @RequestBody UserInputDTO dto) {
    UserFullOutputDTO user = userService.update(id, dto);
    return ResponseEntity.ok(user);
  }
}
