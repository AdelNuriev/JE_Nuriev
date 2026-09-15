package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.client.UserServiceClient;
import org.example.orderservice.dto.CreateOrderRequest;
import org.example.orderservice.dto.OrderResponse;
import org.example.orderservice.dto.UserDto;
import org.example.orderservice.model.Order;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserServiceClient userServiceClient;
    private final OrderRepository orderRepository;

    public Order create(String token, CreateOrderRequest request) {
        log.atInfo()
                .setMessage("Создание заказа")
                .addKeyValue("product", request.getProduct())
                .log();

        if (request.getProduct() == null || request.getProduct().isBlank()) {
            log.atWarn()
                    .setMessage("Продукт пустой")
                    .log();
            throw new IllegalArgumentException("product is required");
        }

        String username = userServiceClient.resolveUsername(token);
        Order order = orderRepository.save(username, request.getProduct());

        log.atInfo()
                .setMessage("Заказ создан")
                .addKeyValue("orderId", order.getOrderId())
                .log();

        return order;
    }

    public List<Order> list(String token) {
        log.atInfo()
                .setMessage("Запрос списка заказов")
                .log();

        String username = userServiceClient.resolveUsername(token);
        List<Order> orders = orderRepository.findByUsername(username);

        log.atInfo()
                .setMessage("Всего заказов на пользователя")
                .addKeyValue("orders", orders.size())
                .addKeyValue("username", username)
                .log();

        return orders;
    }

    public void delete(String token, long id) {
        log.atInfo()
                .setMessage("Удаление заказа")
                .addKeyValue("orderId", id)
                .log();

        String username = userServiceClient.resolveUsername(token);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.atWarn()
                            .setMessage("Заказ не найден при удалении")
                            .addKeyValue("orderId", id)
                            .log();
                    return new IllegalArgumentException("order not found");
                });

        orderRepository.remove(id);

        log.atInfo()
                .setMessage("Заказ удален")
                .addKeyValue("orderId", id)
                .log();
    }
}