package com.sparta.instahub.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // post게시물 에러
    @ExceptionHandler(InaccessiblePostException.class)
    public ResponseEntity<String> inaccessiblePostException(String message) {
        return ResponseEntity.status(401).body(message);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> UnauthorizedExceptionHandler(String message) {
        return ResponseEntity.status(401).body(message);
    }

    @ExceptionHandler(InaccessibleImageException.class)
    public ResponseEntity<String> InaccessibleImageExceptionHandler(String message) {
        return ResponseEntity.status(401).body(message);
    }
}
