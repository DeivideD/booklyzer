package com.pge.booklyzer.application.controller;

import com.pge.booklyzer.application.dto.book.input.BookInputDTO;
import com.pge.booklyzer.application.dto.book.output.BookFullOutputDTO;
import com.pge.booklyzer.application.dto.book.output.BookOutputDTO;
import com.pge.booklyzer.domain.filter.BookFilter;
import com.pge.booklyzer.domain.service.BookService;
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
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "API para gerenciamento de livros")
public class BookController {

  private final BookService bookService;

  @Operation(
          summary = "Listar todos os livros",
          description = "Retorna uma lista paginada de livros com possibilidade de filtros",
          parameters = {
                  @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
                  @Parameter(name = "size", description = "Itens por página", example = "10"),
                  @Parameter(name = "sort", description = "Critérios de ordenação (formato: propriedade,asc|desc)", example = "title,asc"),
                  @Parameter(name = "title", description = "Filtrar por título do livro (busca parcial)", example = "Dom"),
                  @Parameter(name = "ISBN", description = "Filtrar por ISBN exato", example = "978-3-16-148410-0"),
                  @Parameter(name = "authorId", description = "Filtrar por ID do autor", example = "123e4567-e89b-12d3-a456-426614174000"),
                  @Parameter(name = "available", description = "Filtrar por pelos que estão disponíveis true/false", example = "true")
          }
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Livros listados com sucesso",
                  content = @Content(schema = @Schema(implementation = BookOutputDTO.class))),
          @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
  })
  @GetMapping
  public ResponseEntity<Page<BookOutputDTO>> findAll(
          @Parameter(hidden = true)
          @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable,
          @Parameter(hidden = true) BookFilter filter) {

    Page<BookOutputDTO> books = bookService.findAll(pageable, filter);
    return ResponseEntity.ok(books);
  }

  @Operation(
          summary = "Buscar livro por ID",
          description = "Retorna um único livro pelo seu identificador único",
          responses = {
                  @ApiResponse(responseCode = "200", description = "Livro encontrado",
                          content = @Content(schema = @Schema(implementation = BookFullOutputDTO.class))),
                  @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
                  @ApiResponse(responseCode = "400", description = "Formato de ID inválido")
          }
  )
  @GetMapping("/{id}")
  public ResponseEntity<BookFullOutputDTO> findById(
          @Parameter(description = "UUID do livro a ser buscado",
                  example = "123e4567-e89b-12d3-a456-426614174000",
                  required = true)
          @PathVariable UUID id) {

    BookFullOutputDTO book = bookService.findById(id);
    return ResponseEntity.ok(book);
  }

  @Operation(
          summary = "Criar novo livro",
          description = "Cadastra um novo livro com os dados fornecidos",
          requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Dados do livro a ser criado",
                  required = true,
                  content = @Content(schema = @Schema(implementation = BookInputDTO.class)))
  )
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "Livro criado com sucesso",
                  content = @Content(schema = @Schema(implementation = BookFullOutputDTO.class))),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "409", description = "Livro já existe"),
          @ApiResponse(responseCode = "404", description = "Autor não encontrado ao vincular livro")
  })
  @PostMapping
  public ResponseEntity<BookFullOutputDTO> create(
          @Valid @RequestBody BookInputDTO dto) {

    BookFullOutputDTO savedBook = bookService.save(dto);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedBook.getId())
            .toUri();
    return ResponseEntity.created(location).body(savedBook);
  }

  @Operation(
          summary = "Atualizar livro",
          description = "Atualiza os dados de um livro existente",
          requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Dados atualizados do livro",
                  required = true,
                  content = @Content(schema = @Schema(implementation = BookInputDTO.class)))
  )
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                  content = @Content(schema = @Schema(implementation = BookFullOutputDTO.class))),
          @ApiResponse(responseCode = "400", description = "Dados inválidos"),
          @ApiResponse(responseCode = "404", description = "Livro ou autor não encontrado")
  })
  @PutMapping("/{id}")
  public ResponseEntity<BookFullOutputDTO> update(
          @Parameter(description = "UUID do livro a ser atualizado",
                  example = "123e4567-e89b-12d3-a456-426614174000",
                  required = true)
          @PathVariable UUID id,
          @Valid @RequestBody BookInputDTO dto) {

    BookFullOutputDTO updatedBook = bookService.update(id, dto);
    return ResponseEntity.ok(updatedBook);
  }
}