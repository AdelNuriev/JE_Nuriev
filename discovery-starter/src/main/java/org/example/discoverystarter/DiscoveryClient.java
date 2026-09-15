package org.example.discoverystarter;

import org.example.discoverystarter.dto.DiscoverResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

public class DiscoveryClient {

    private final WebClient webClient;

    public DiscoveryClient(String serverUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(serverUrl)
                .build();
    }

    public URI getInstance(String serviceName) {
        DiscoverResponse response = webClient.get()
                .uri("/discover/{name}", serviceName)
                .retrieve()
                .bodyToMono(DiscoverResponse.class)
                .block();

        if (response == null) {
            throw new IllegalStateException("Service not found: " + serviceName);
        }

        return URI.create("http://" + response.getHost() + ":" + response.getPort());
    }
}