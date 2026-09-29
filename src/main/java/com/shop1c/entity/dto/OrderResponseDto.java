package com.shop1c.entity.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private Long customerId;
    private String customerCode;
    private String customerName;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;

}
