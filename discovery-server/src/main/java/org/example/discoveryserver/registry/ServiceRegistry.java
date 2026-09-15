package org.example.discoveryserver.registry;

import org.example.discoveryserver.dto.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ServiceRegistry {

    private final Map<String, List<ServiceInstance>> registry = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public void register(String serviceName, ServiceInstance instance) {
        List<ServiceInstance> instances = registry.computeIfAbsent(
                serviceName,
                key -> new CopyOnWriteArrayList<>()
        );

        if (!instances.contains(instance)) {
            instances.add(instance);
        }

        counters.putIfAbsent(serviceName, new AtomicInteger(0));
    }

    public ServiceInstance discover(String serviceName) {
        List<ServiceInstance> instances = registry.get(serviceName);

        if (instances == null || instances.isEmpty()) {
            return null;
        }

        AtomicInteger counter = counters.computeIfAbsent(
                serviceName,
                key -> new AtomicInteger(0)
        );

        int index = Math.floorMod(counter.getAndIncrement(), instances.size());
        return instances.get(index);
    }

    public void removeInstance(String serviceName, ServiceInstance instance) {
        List<ServiceInstance> instances = registry.get(serviceName);

        if (instances == null) {
            return;
        }

        instances.remove(instance);

        if (instances.isEmpty()) {
            registry.remove(serviceName);
            counters.remove(serviceName);
        }
    }

    public Map<String, List<ServiceInstance>> getAll() {
        return registry;
    }
}