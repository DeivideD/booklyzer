package com.pge.booklyzer.shared.exceptions.handler;

import com.pge.booklyzer.domain.exception.DomainException;
import com.pge.booklyzer.shared.exceptions.BadRequestException;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatusCode status,
          WebRequest request) {

    List<ApiError.ValidationError> validationErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> new ApiError.ValidationError(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()))
            .toList();

    ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Erro de validação",
            LocalDateTime.now(),
            extractPath(request),
            validationErrors);

    return ResponseEntity.badRequest().body(error);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
          HttpMediaTypeNotAcceptableException ex,
          HttpHeaders headers,
          HttpStatusCode status,
          WebRequest request) {

    return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new ApiError(
                    HttpStatus.NOT_ACCEPTABLE.value(),
                    "Tipo de mídia não aceitável. Use 'application/json'",
                    LocalDateTime.now(),
                    extractPath(request)
            ));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, WebRequest request) {
    ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now(),
            extractPath(request)
    );
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(error);
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ApiError> handleDomainException
          (DomainException ex, WebRequest request) {
    ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now(),
            extractPath(request)
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, WebRequest request) {
    ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now(),
            extractPath(request)
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
    ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            "Credenciais inválidas",
            LocalDateTime.now(),
            extractPath(request)
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
    ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Credenciais inválidas",
            LocalDateTime.now(),
            extractPath(request)
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }


  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
    ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Falha na autenticação",
            LocalDateTime.now(),
            extractPath(request)
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request) {
    ApiError error = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Ocorreu um erro interno",
            LocalDateTime.now(),
            extractPath(request)
    );
    logger.error("Erro interno não tratado", ex);
    return ResponseEntity.internalServerError().body(error);
  }

  private String extractPath(WebRequest request) {
    return request.getDescription(false).replace("uri=", "");
  }

}