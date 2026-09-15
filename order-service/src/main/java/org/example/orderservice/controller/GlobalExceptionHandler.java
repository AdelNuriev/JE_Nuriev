package org.example.orderservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResponse> badRequest(IllegalArgumentException e) {
        log.atError()
                .setMessage("Ошибка запроса")
                .setCause(e)
                .log();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<MessageResponse> forbidden(IllegalStateException e) {
        log.atError()
                .setMessage("Конфликт состояния")
                .setCause(e)
                .log();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new MessageResponse(e.getMessage()));
    }
}
