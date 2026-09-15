package org.example.orderservice.client;

import org.example.orderservice.dto.UserDto;

import java.util.List;

public interface UserServiceClient {

    String resolveUsername(String token);
}