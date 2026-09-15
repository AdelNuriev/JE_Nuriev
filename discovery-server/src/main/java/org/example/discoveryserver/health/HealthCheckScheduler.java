package org.example.discoveryserver.health;

import lombok.extern.slf4j.Slf4j;
import org.example.discoveryserver.dto.ServiceInstance;
import org.example.discoveryserver.registry.ServiceRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HealthCheckScheduler {

    private final ServiceRegistry registry;
    private final WebClient webClient;

    public HealthCheckScheduler(ServiceRegistry registry, WebClient webClient) {
        this.registry = registry;
        this.webClient = webClient;
    }

    @Scheduled(fixedRate = 10_000)
    public void checkAll() {
        for (Map.Entry<String, List<ServiceInstance>> entry : registry.getAll().entrySet()) {
            String serviceName = entry.getKey();

            for (ServiceInstance instance : entry.getValue()) {
                String url = "http://" + instance.getHost() + ":" + instance.getPort()
                        + "/actuator/health";

                try {
                    webClient.get()
                            .uri(url)
                            .retrieve()
                            .toBodilessEntity()
                            .timeout(Duration.ofSeconds(3))
                            .block();
                } catch (Exception e) {
                    log.atWarn()
                            .setMessage("Инстанс упал")
                            .addKeyValue("serviceName", serviceName)
                            .addKeyValue("host", instance.getHost())
                            .addKeyValue("port", instance.getPort())
                            .log();
                    registry.removeInstance(serviceName, instance);
                }
            }
        }
    }
}