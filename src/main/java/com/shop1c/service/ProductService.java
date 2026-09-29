package com.shop1c.service;

import com.shop1c.entity.dto.ProductDto;

import java.util.List;

public interface ProductService {

//    void updateProductsFrom1C();
//    List<Product> getAllProducts();
//    String testConnection();
    List<ProductDto> getProductsFrom1C(Long codeClient);
}
