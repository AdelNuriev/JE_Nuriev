package org.example.orderservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.discoverystarter.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpUserServiceClient implements UserServiceClient {

    private final DiscoveryClient discoveryClient;
    private final WebClient webClient;

    @Override
    public String resolveUsername(String token) {
        log.atInfo()
                .setMessage("Запрос на имя пользователя")
                .log();

        URI userServiceUri = discoveryClient.getInstance("user-service");

        try {
            Map<?, ?> body = webClient.get()
                    .uri(URI.create(userServiceUri + "/account"))
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            String message = Optional.ofNullable(body)
                    .map(b -> b.get("message"))
                    .map(Object::toString)
                    .orElseThrow(() -> {
                        log.atWarn()
                                .setMessage("Пустой токен")
                                .log();
                        return new IllegalArgumentException("invalid token");
                    });

            String username = message.replace("Hi, ", "").trim();

            return username;
        } catch (WebClientResponseException e) {
            log.atError()
                    .setMessage("Токен отклонен")
                    .addKeyValue("token", token)
                    .log();
            throw new IllegalArgumentException("invalid token");
        } catch (Exception e) {
            log.atError()
                    .setMessage("Ошибка при обращении к сервису пользователя")
                    .setCause(e)
                    .log();
            throw new IllegalArgumentException("user service is unavailable");
        }

    }
}