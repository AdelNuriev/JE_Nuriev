package org.example.discoveryserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.discoveryserver.dto.DiscoverResponse;
import org.example.discoveryserver.dto.RegisterRequest;
import org.example.discoveryserver.dto.RegisterResponse;
import org.example.discoveryserver.dto.ServiceInstance;
import org.example.discoveryserver.registry.ServiceRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscoveryController {

    private final ServiceRegistry registry;

    public DiscoveryController(ServiceRegistry registry) {
        this.registry = registry;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request,
                                     HttpServletRequest httpRequest) {
        String host = httpRequest.getRemoteHost();

        if ("127.0.0.1".equals(host) || "0:0:0:0:0:0:0:1".equals(host)) {
            host = "localhost";
        }

        registry.register(
                request.getServiceName(),
                new ServiceInstance(host, request.getPort())
        );

        return new RegisterResponse("registered");
    }

    @GetMapping("/discover/{serviceName}")
    public ResponseEntity<DiscoverResponse> discover(@PathVariable String serviceName) {
        ServiceInstance instance = registry.discover(serviceName);

        if (instance == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new DiscoverResponse(instance.getHost(), instance.getPort())
        );
    }
}