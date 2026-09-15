package org.example.userservice.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.userservice.model.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class UserRepository {

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, String> usernameByToken = new ConcurrentHashMap<>();

    public boolean exists(String username) {
        return users.containsKey(username);
    }

    public void save(User user) {
        users.put(user.getUsername(), user);

        log.atInfo()
                .setMessage("Пользователь сохранен")
                .addKeyValue("username", user.getUsername())
                .log();
    }

    public Optional<User> findByUsername(String username) {
        User user = users.get(username);

        if (user == null) {
            log.atWarn()
                .setMessage("Пользователь не найден")
                .addKeyValue("username", username)
                .log();

            return Optional.empty();
        }

        return Optional.of(user);
    }

    public String createToken(String username) {
        String token = UUID.randomUUID().toString();
        usernameByToken.put(token, username);

        log.atInfo()
                .setMessage("Для пользователя создан токен")
                .addKeyValue("username", username)
                .addKeyValue("token", token)
                .log();

        return token;
    }

    public Optional<String> findUsernameByToken(String token) {
        String username = usernameByToken.get(token);

        if (username == null) {
            log.atWarn()
                    .setMessage("Токен не найден")
                    .addKeyValue("token", token)
                    .log();
            return Optional.empty();
        }

        return Optional.of(username);
    }
}
