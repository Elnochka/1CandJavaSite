package com.shop1c.service;

import com.shop1c.entity.dto.OrderRequestDto;
import com.shop1c.entity.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto createOrder(Long customerId, OrderRequestDto request);

    List<OrderResponseDto> getMyOrders(Long customerId);

    List<OrderResponseDto> getAllOrders();


}
