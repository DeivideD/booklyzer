package com.pge.booklyzer.application.controller;

import com.pge.booklyzer.application.dto.bookloan.input.BookLoanInputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanFullOutputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanOutputDTO;
import com.pge.booklyzer.domain.service.BookLoanService;
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
@RequestMapping("loans")
@RequiredArgsConstructor
@Tag(name = "Empréstimos de livros", description = "Operações relacionadas a empréstimos de livros")
public class BookLoanController {
  @Operation(
          summary = "Registrar devolução",
          description = "Registra a devolução de um livro emprestado",
          responses = {
                  @ApiResponse(responseCode = "200", description = "Devolução registrada com sucesso",
                          content = @Content(schema = @Schema(implementation = BookLoanFullOutputDTO.class))),
                  @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado"),
                  @ApiResponse(responseCode = "400", description = "ID inválido ou empréstimo já devolvido")
          }
  )
  @PutMapping("/{loanId}/return-book")
  public ResponseEntity<BookLoanFullOutputDTO> returnBook(
          @Parameter(description = "ID do empréstimo a ser devolvido",
                  example = "123e4567-e89b-12d3-a456-426614174000",
                  required = true)
          @PathVariable UUID loanId) {

    BookLoanFullOutputDTO returnedLoan = bookLoanService.returnBook(loanId);
    return ResponseEntity.ok(returnedLoan);
  }


  private final BookLoanService bookLoanService;

  @Operation(
          summary = "Realizar novo empréstimo",
          description = "Registra um novo empréstimo de livro no sistema",
          requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Dados necessários para registro do empréstimo",
                  required = true,
                  content = @Content(schema = @Schema(implementation = BookLoanInputDTO.class)))
  )
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "Empréstimo registrado com sucesso",
                  content = @Content(schema = @Schema(implementation = BookLoanFullOutputDTO.class))),
          @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
          @ApiResponse(responseCode = "404", description = "Livro ou usuário não encontrado"),
          @ApiResponse(responseCode = "409", description = "Conflito: livro já emprestado")
  })
  @PostMapping
  public ResponseEntity<BookLoanFullOutputDTO> loanBook(
          @Valid @RequestBody BookLoanInputDTO loanDTO) {

    BookLoanFullOutputDTO createdLoan = bookLoanService.loanBook(loanDTO);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdLoan.getId())
            .toUri();

    return ResponseEntity.created(location).body(createdLoan);
  }

  @Operation(
          summary = "Consultar empréstimos do usuário",
          description = "Retorna uma lista paginada de empréstimos associados a um usuário específico",
          parameters = {
                  @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
                  @Parameter(name = "size", description = "Itens por página", example = "10"),
                  @Parameter(name = "sort", description = "Critérios de ordenação (formato: propriedade,asc|desc)", example = "loanDate,desc")
          },
          responses = {
                  @ApiResponse(responseCode = "200", description = "Lista paginada de empréstimos retornada com sucesso",
                          content = @Content(schema = @Schema(implementation = BookLoanOutputDTO.class))),
                  @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                  @ApiResponse(responseCode = "400", description = "ID inválido fornecido ou parâmetros de paginação inválidos")
          }
  )
  @GetMapping("/user/{userId}")
  public ResponseEntity<Page<BookLoanOutputDTO>> getUserLoans(
          @Parameter(description = "ID do usuário para consulta",
                  example = "123e4567-e89b-12d3-a456-426614174000",
                  required = true)
          @PathVariable UUID userId,
          @Parameter(hidden = true)
          @PageableDefault(page = 0, size = 10, sort = "loanDate", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<BookLoanOutputDTO> loans = bookLoanService.getUserLoans(pageable, userId);
    return ResponseEntity.ok(loans);
  }

  @Operation(
          summary = "Listar empréstimos ativos",
          description = "Retorna uma lista paginada de empréstimos que ainda não foram devolvidos",
          parameters = {
                  @Parameter(name = "page", description = "Número da página (começa em 0)", example = "0"),
                  @Parameter(name = "size", description = "Itens por página", example = "10"),
                  @Parameter(name = "sort", description = "Critérios de ordenação (formato: propriedade,asc|desc)", example = "loanDate,desc")
          },
          responses = {
                  @ApiResponse(responseCode = "200", description = "Lista paginada de empréstimos ativos retornada",
                          content = @Content(schema = @Schema(implementation = BookLoanOutputDTO.class))),
                  @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
          }
  )
  @GetMapping("/actives")
  public ResponseEntity<Page<BookLoanOutputDTO>> getActiveLoans(
          @Parameter(hidden = true)
          @PageableDefault(page = 0, size = 10, sort = "loanDate", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<BookLoanOutputDTO> activeLoans = bookLoanService.getActiveLoans(pageable);
    return ResponseEntity.ok(activeLoans);
  }

}