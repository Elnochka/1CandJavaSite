package com.shop1c.controller;

import com.shop1c.entity.Users;
import com.shop1c.entity.dto.ProductDto;
import com.shop1c.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getProducts(Authentication authentication) {

        Users user = (Users) authentication.getPrincipal();
        log.info("Get products. userId={}", user.getId());

        return productService.getProductsFrom1C(user.getId());
    }

//    @PostMapping("/sync")
//    public String syncProducts() {
//
//        productService.updateProductsFrom1C();
//
//        return "Products synchronized successfully";
//    }
//
//    @GetMapping("/test")
//    public String test() {
//
//        return productService.testConnection();
//
//    }
}
