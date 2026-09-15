package org.example.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.LoginResponse;
import org.example.userservice.dto.MessageResponse;
import org.example.userservice.dto.RegisterRequest;
import org.example.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public MessageResponse register(@RequestBody RegisterRequest request) {
        log.atInfo()
                .setMessage("Запрос на регистрацию")
                .addKeyValue("username", request.getUsername())
                .log();

        userService.register(request);
        return new MessageResponse("Зарегистрирован");
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        log.atInfo()
                .setMessage("Запрос на вход")
                .addKeyValue("username", request.getUsername())
                .log();

        return userService.login(request);
    }

    @GetMapping("/account")
    public MessageResponse account(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", "");

        log.atInfo()
                .setMessage("Запрос на аккаунт")
                .log();

        String username = userService.getUsernameByToken(token);
        return new MessageResponse("Hi, " + username);
    }
}