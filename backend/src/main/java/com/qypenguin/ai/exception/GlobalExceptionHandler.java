package com.qypenguin.ai.exception;

import com.qypenguin.ai.domain.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
  public ResponseEntity<ApiResponse<Void>> handleValidation(Exception ex) {
    return ResponseEntity.badRequest().body(ApiResponse.fail("参数校验失败"));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(ApiResponse.fail(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleAny(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.fail("服务暂时不可用，请稍后再试"));
  }
}
