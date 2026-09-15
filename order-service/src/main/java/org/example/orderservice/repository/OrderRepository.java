package org.example.orderservice.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class OrderRepository {

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    public Order save(String username, String product) {
        long id = counter.getAndIncrement();

        Order order = Order.builder()
                .orderId(id)
                .username(username)
                .product(product)
                .build();

        orders.put(id, order);

        log.atInfo()
                .setMessage("Заказ сохранен")
                .addKeyValue("orderId", id)
                .addKeyValue("username", username)
                .addKeyValue("product", product)
                .log();

        return order;
    }

    public Optional<Order> findById(long id) {
        Order order = orders.get(id);

        if (order == null) {
            log.atWarn()
                .setMessage("Заказ не найден")
                .addKeyValue("orderId", id)
                .log();

            return Optional.empty();
        }

        return Optional.of(order);
    }

    public boolean remove(long id) {
        boolean removed = orders.remove(id) != null;

        if (removed) {
            log.atInfo()
                    .setMessage("Заказ удален")
                    .addKeyValue("orderId", id)
                    .log();
        } else {
            log.atWarn()
                    .setMessage("Не удалось удалить заказ")
                    .addKeyValue("orderId", id)
                    .log();
        }

        return removed;
    }

    public List<Order> findByUsername(String username) {
        List<Order> result = orders.values().stream()
                .filter(o -> o.getUsername().equals(username))
                .toList();

        log.atInfo()
                .setMessage("Найдены заказы для пользователя")
                .addKeyValue("username", username)
                .addKeyValue("count", result.size())
                .log();

        return result;
    }

}
