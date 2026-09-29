package com.shop1c.entity.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private Long customerId;
    private String customerCode;//
    private String customerName;//
    private List<OrderItemRequestDto> items;
}
