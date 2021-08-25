package com.example.demo.controller;

import com.example.demo.dto.response.Response;
import com.example.demo.exception.InvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity badCrentials(Exception e) {
        Response response = Response.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("로그인 실패")
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity invalidRefreshToken(Exception e) {
        Response response = Response.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
