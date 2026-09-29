package com.shop1c.service.impl;

import com.shop1c.entity.Users;
import com.shop1c.entity.dto.ProductDto;
import com.shop1c.repository.ProductRepository;
import com.shop1c.repository.UsersRepository;
import com.shop1c.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final RestClient oneCRestClient;
    private final UsersRepository usersRepository;

    @Override
    public List<ProductDto> getProductsFrom1C(Long userId) {

        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String customerCode = users.getCode1C();

        ProductDto[] products = oneCRestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/products")
                        .queryParam("customerCode", customerCode)
                        .build())
                .retrieve()
                .body(ProductDto[].class);

        log.info("Products got successfully. userId={}", userId);

        if (products == null) {
            return List.of();
        }

        return Arrays.asList(products);
    }

//    @Override
//    public void updateProductsFrom1C() {
//        ProductDto[] products = oneCRestClient
//                .get()
//                .uri("/products")
//                .retrieve()
//                .body(ProductDto[].class);
//
//        if (products == null) {
//            return;
//        }
//
//        for (ProductDto productFrom1C : products) {
//
//            Product product = productRepository
//                    .findByProductCode(productFrom1C.getProductCode())
//                    .orElseGet(Product::new);
//
//            product.setProductCode(productFrom1C.getProductCode());
//            product.setProductName(productFrom1C.getProductName());
//            product.setPrice(productFrom1C.getPrice());
//            product.setStock(productFrom1C.getStock());
//            product.setActive(true);
//
//            productRepository.save(product);
//        }
//    }
//
//    @Override
//    public List<Product> getAllProducts() {
//
//        return productRepository.findAll();
//
//    }
//
//    public String testConnection() {
//
//        String response = oneCRestClient
//                .get()
//                .uri("/test")
//                .retrieve()
//                .body(String.class);
//
//        System.out.println("RESPONSE FROM 1C = [" + response + "]");
//
//        return response;
//    }
}
