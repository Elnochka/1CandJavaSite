package com.shop1c.controller;

import com.shop1c.entity.Users;
import com.shop1c.entity.dto.OrderRequestDto;
import com.shop1c.entity.dto.OrderResponseDto;
import com.shop1c.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto createOrder(
            Authentication authentication,
            @RequestBody OrderRequestDto request
    ) {
        Users user = (Users) authentication.getPrincipal();
        log.info("Creating order. userId={}", user.getId());

        return orderService.createOrder(user.getId(), request);
    }

    @GetMapping("/my")
    public List<OrderResponseDto> getMyOrders(Authentication authentication) {
        Users user = (Users) authentication.getPrincipal();

        return orderService.getMyOrders(user.getId());
    }

    @GetMapping("/all")
    public List<OrderResponseDto> getAllOrders(Authentication authentication) {
        Users user = (Users) authentication.getPrincipal();

        if (!"ADMIN".equals(user.getRole().toValue())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied"
            );
        }

        return orderService.getAllOrders();
    }

}
