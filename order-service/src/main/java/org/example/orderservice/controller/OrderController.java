package org.example.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.dto.CreateOrderRequest;
import org.example.orderservice.dto.MessageResponse;
import org.example.orderservice.dto.OrderResponse;
import org.example.orderservice.model.Order;
import org.example.orderservice.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public Order create(@RequestHeader("Authorization") String authorization, @RequestBody CreateOrderRequest createOrderRequest) {
        log.atInfo()
                .setMessage("Запрос на создание заказа")
                .addKeyValue("product", createOrderRequest.getProduct())
                .log();

        String token = authorization.replace("Bearer ", "");
        return orderService.create(token, createOrderRequest);
    }

    @GetMapping("/orders")
    public List<Order> getOrders(@RequestHeader("Authorization") String authorization) {
        log.atInfo()
                .setMessage("Запрос на получение списка заказа")
                .log();

        String token = authorization.replace("Bearer ", "");
        return orderService.list(token);
    }

    @DeleteMapping("/orders/{id}")
    public MessageResponse delete(@RequestHeader("Authorization") String authorization, @PathVariable Long id) {
        log.atInfo()
                .setMessage("Запрос на удаление заказа")
                .addKeyValue("orderId", id)
                .log();

        String token = authorization.replace("Bearer ", "");
        orderService.delete(token, id);
        return new MessageResponse("deleted");
    }
}