package com.pge.booklyzer.application.controller;

import com.pge.booklyzer.application.dto.author.input.AuthorInputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorFullOutputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorOutputDTO;
import com.pge.booklyzer.domain.filter.AuthorFilter;
import com.pge.booklyzer.domain.service.AuthorService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "API para gerenciamento de autores")
public class AuthorController {

  private final AuthorService authorService;

  @Operation(
          summary = "Listar todos os autores",
          description = "Retorna uma lista paginada de autores com possibilidade de filtro por nome",
          parameters = {
                  @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
                  @Parameter(name = "size", description = "Quantidade de itens por página", example = "10"),
                  @Parameter(name = "sort", description = "Critérios de ordenação (formato: propriedade,asc|desc)", example = "name,asc"),
                  @Parameter(name = "name", description = "Filtro por nome do autor (opcional)", example = "Machado de Assis")
          }
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Autores listados com sucesso",
                  content = @Content(schema = @Schema(implementation = AuthorOutputDTO.class))),
          @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
  })
  @GetMapping
  public ResponseEntity<Page<AuthorOutputDTO>> findAll(
          @Parameter(hidden = true)
          @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
          @Parameter(hidden = true) AuthorFilter filter) {

    Page<AuthorOutputDTO> authors = authorService.findAll(pageable, filter);
    return ResponseEntity.ok(authors);
  }

  @Operation(
          summary = "Buscar autor por ID",
          description = "Retorna um autor específico com base no seu identificador único",
          responses = {
                  @ApiResponse(responseCode = "200", description = "Autor encontrado",
                          content = @Content(schema = @Schema(implementation = AuthorFullOutputDTO.class))),
                  @ApiResponse(responseCode = "404", description = "Autor não encontrado"),
                  @ApiResponse(responseCode = "400", description = "ID inválido")
          }
  )
  @GetMapping("/{id}")
  public ResponseEntity<AuthorFullOutputDTO> findById(
          @Parameter(description = "UUID do autor a ser buscado",
                  example = "123e4567-e89b-12d3-a456-426614174000",
                  required = true)
          @PathVariable UUID id) {

    AuthorFullOutputDTO autor = authorService.findById(id);
    return ResponseEntity.ok(autor);
  }

  @Operation(
          summary = "Cadastrar novo autor",
          description = "Cria um novo registro de autor com os dados fornecidos",
          requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Dados do autor a ser criado",
                  required = true,
                  content = @Content(schema = @Schema(implementation = AuthorInputDTO.class)))
          )
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "Autor criado com sucesso",
                  content = @Content(schema = @Schema(implementation = AuthorFullOutputDTO.class))),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "409", description = "Autor já existe")
  })
  @PostMapping
  public ResponseEntity<AuthorFullOutputDTO> save(
          @Valid @RequestBody AuthorInputDTO dto) {

    AuthorFullOutputDTO autorSalvo = authorService.save(dto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(autorSalvo.getId())
            .toUri();
    return ResponseEntity.created(location).body(autorSalvo);
  }

  @Operation(
          summary = "Atualizar autor",
          description = "Atualiza os dados de um autor existente",
          requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Dados atualizados do autor",
                  required = true,
                  content = @Content(schema = @Schema(implementation = AuthorInputDTO.class)))
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso",
                  content = @Content(schema = @Schema(implementation = AuthorFullOutputDTO.class))),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "404", description = "Autor não encontrado")
  })
  @PutMapping("/{id}")
  public ResponseEntity<AuthorFullOutputDTO> update(
          @Parameter(description = "UUID do autor a ser atualizado",
                  example = "123e4567-e89b-12d3-a456-426614174000",
                  required = true)
          @PathVariable UUID id,
          @Valid @RequestBody AuthorInputDTO dto) {

    AuthorFullOutputDTO autorAtualizado = authorService.update(id, dto);
    return ResponseEntity.ok(autorAtualizado);
  }
}