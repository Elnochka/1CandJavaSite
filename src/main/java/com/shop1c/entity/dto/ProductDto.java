package com.shop1c.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String productCode;
    private String productName;
    private BigDecimal price;
    private BigDecimal stock;


}
