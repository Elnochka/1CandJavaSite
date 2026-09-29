package com.shop1c.service.impl;

import com.shop1c.entity.Order;
import com.shop1c.entity.OrderItem;
import com.shop1c.entity.Users;
import com.shop1c.entity.dto.OrderItemRequestDto;
import com.shop1c.entity.dto.OrderItemResponseDto;
import com.shop1c.entity.dto.OrderRequestDto;
import com.shop1c.entity.dto.OrderResponseDto;
import com.shop1c.repository.OrderRepository;
import com.shop1c.repository.UsersRepository;
import com.shop1c.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UsersRepository usersRepository;

    private OrderResponseDto toResponse(Order order) {

        List<OrderItemResponseDto> items = order.getItems()
                .stream()
                .map(item -> OrderItemResponseDto.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productCode(item.getProductCode())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .amount(item.getAmount())
                        .build())
                .toList();

        return OrderResponseDto.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .customerCode(order.getCustomerCode())
                .customerName(order.getCustomerName())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(
            Long userId,
            OrderRequestDto request
    ) {

        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String customerCode = users.getCode1C();
        String customerName = users.getName1C();
        Order order = Order.builder()
                .customerId(userId)
                .customerCode(customerCode)
                .customerName(customerName)
                .createdAt(LocalDateTime.now())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDto itemRequest : request.getItems()) {

            Long productId = itemRequest.getProductId();
            String productCode = itemRequest.getProductCode();
            String productName = itemRequest.getProductName();
            BigDecimal price = itemRequest.getPrice();
            BigDecimal amount = price.multiply(itemRequest.getQuantity());


            OrderItem item = OrderItem.builder()
                    .order(order)
                    .productId(productId)
                    .productCode(productCode)
                    .productName(productName)
                    .quantity(itemRequest.getQuantity())
                    .price(price)
                    .amount(amount)
                    .build();

            order.getItems().add(item);

            total = total.add(amount);
        }

        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        log.info("Order created successfully. userId={}", userId);

        return toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getMyOrders(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }


}
