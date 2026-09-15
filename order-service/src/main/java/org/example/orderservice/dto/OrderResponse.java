package org.example.orderservice.dto;

import java.util.List;

public class OrderResponse {

    private long orderId;
    private List<UserDto> usersUsed;

    public OrderResponse() {
    }

    public OrderResponse(long orderId, List<UserDto> usersUsed) {
        this.orderId = orderId;
        this.usersUsed = usersUsed;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public List<UserDto> getUsersUsed() {
        return usersUsed;
    }

    public void setUsersUsed(List<UserDto> usersUsed) {
        this.usersUsed = usersUsed;
    }
}