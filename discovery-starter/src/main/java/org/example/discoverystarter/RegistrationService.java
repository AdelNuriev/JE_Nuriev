package org.example.discoverystarter;

import org.example.discoverystarter.dto.RegisterResponse;
import org.example.discoverystarter.dto.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.WebClient;

public class RegistrationService {

    private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);

    private final DiscoveryProperties props;
    private final WebClient webClient;

    public RegistrationService(DiscoveryProperties props) {
        this.props = props;
        this.webClient = WebClient.builder()
                .baseUrl(props.getServerUrl())
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void register() {
        try {
            RegistrationRequest request = new RegistrationRequest(
                    props.getServiceName(),
                    props.getServicePort()
            );

            webClient.post()
                    .uri("/register")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(RegisterResponse.class)
                    .block();

            log.info(
                    "Registered {}:{} in discovery server",
                    props.getServiceName(),
                    props.getServicePort()
            );
        } catch (Exception e) {
            log.error("Registration failed", e);
        }
    }
}