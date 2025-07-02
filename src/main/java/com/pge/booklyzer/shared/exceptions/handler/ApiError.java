package com.pge.booklyzer.shared.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
  @JsonProperty("status")
  private int status;

  @JsonProperty("message")
  private String message;

  @JsonProperty("timestamp")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime timestamp;

  @JsonProperty("path")
  private String path;

  @JsonProperty("validationErrors")
  private List<ValidationError> validationErrors;

  public ApiError(int status, String message, LocalDateTime timestamp, String path) {
    this.status = status;
    this.message = message;
    this.timestamp = timestamp;
    this.path = path;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class ValidationError {
    @JsonProperty("field")
    private String field;

    @JsonProperty("error")
    private String error;
  }
}