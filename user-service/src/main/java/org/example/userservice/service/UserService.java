package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.LoginResponse;
import org.example.userservice.dto.RegisterRequest;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(RegisterRequest request) {
        log.atInfo()
                .setMessage("Регистрация пользователя")
                .addKeyValue("username", request.getUsername())
                .log();

        if (request.getUsername() == null || request.getUsername().isBlank()) {
            log.atWarn()
                    .setMessage("Имя пользователя пустое")
                    .log();

            throw new IllegalArgumentException("username is required");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            log.atWarn()
                    .setMessage("Пароль пустой")
                    .addKeyValue("username", request.getUsername())
                    .log();

            throw new IllegalArgumentException("password is required");
        }

        if (userRepository.exists(request.getUsername())) {
            log.atWarn()
                    .setMessage("Пользователь уже существует")
                    .addKeyValue("username", request.getUsername())
                    .log();

            throw new IllegalArgumentException("user already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        userRepository.save(user);

        log.atInfo()
                .setMessage("Пользователь сохранен")
                .addKeyValue("username", request.getUsername())
                .log();
    }

    public LoginResponse login(LoginRequest request) {
        log.atInfo()
                .setMessage("Попытка входа")
                .addKeyValue("username", request.getUsername())
                .log();

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.atWarn()
                            .setMessage("Пользовател не найден")
                            .addKeyValue("username", request.getUsername())
                            .log();
                    return new IllegalArgumentException("user not found");
                });

        if (!user.getPassword().equals(request.getPassword())) {
            log.atWarn()
                    .setMessage("Неверный пароль")
                    .addKeyValue("username", request.getUsername())
                    .log();
        }

        String token = userRepository.createToken(user.getUsername());

        log.atInfo()
                .setMessage("Пользователь успешно вошел")
                .addKeyValue("username", request.getUsername())
                .log();

        return new LoginResponse(token);
    }

    public String getUsernameByToken(String token) {

        return userRepository.findUsernameByToken(token)
                .orElseThrow(() -> {
                    log.atWarn()
                            .setMessage("Токен недействителен")
                            .addKeyValue("token", token)
                            .log();
                    return new IllegalArgumentException("invalid token");
                });
    }
}
