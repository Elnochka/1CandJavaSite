package com.shop1c.entity.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {

    private Long productId;
    private String productCode;//
    private String productName;//
    private BigDecimal price;//
    private BigDecimal quantity;
}
