package com.shop1c.service;

import com.shop1c.entity.Users;
import com.shop1c.entity.dto.ProductDto;
import com.shop1c.repository.ProductRepository;
import com.shop1c.repository.UsersRepository;
import com.shop1c.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RestClient oneCRestClient;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private ProductServiceImpl productService;


    @Test
    void getProductsFrom1C_shouldReturnProductsForCustomer() {

        Long userId = 1L;

        Users user = Users.builder()
                .id(userId)
                .code1C("CUST001")
                .name1C("Customer")
                .build();

        ProductDto product1 = ProductDto.builder()
                .productCode("P001")
                .productName("Product 1")
                .price(new BigDecimal("100.00"))
                .stock(new BigDecimal("10"))
                .build();

        ProductDto product2 = ProductDto.builder()
                .productCode("P002")
                .productName("Product 2")
                .price(new BigDecimal("200.00"))
                .stock(new BigDecimal("5"))
                .build();

        when(usersRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(oneCRestClient.get())
                .thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(any(Function.class)))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(ProductDto[].class))
                .thenReturn(new ProductDto[]{
                        product1,
                        product2
                });

        List<ProductDto> result =
                productService.getProductsFrom1C(userId);

        assertThat(result)
                .hasSize(2);

        assertThat(result.get(0).getProductCode())
                .isEqualTo("P001");

        assertThat(result.get(0).getProductName())
                .isEqualTo("Product 1");

        assertThat(result.get(0).getPrice())
                .isEqualByComparingTo("100.00");

        assertThat(result.get(0).getStock())
                .isEqualByComparingTo("10");

        assertThat(result.get(1).getProductCode())
                .isEqualTo("P002");

        verify(usersRepository)
                .findById(userId);

        verify(oneCRestClient)
                .get();
    }


    @Test
    void getProductsFrom1C_shouldReturnEmptyListWhen1CReturnsNull() {

        Long userId = 1L;

        Users user = Users.builder()
                .id(userId)
                .code1C("CUST001")
                .name1C("Customer")
                .build();

        when(usersRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(oneCRestClient.get())
                .thenReturn(requestHeadersUriSpec);

        when(requestHeadersUriSpec.uri(any(Function.class)))
                .thenReturn(requestHeadersSpec);

        when(requestHeadersSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(ProductDto[].class))
                .thenReturn(null);

        List<ProductDto> result =
                productService.getProductsFrom1C(userId);

        assertThat(result)
                .isEmpty();

        verify(usersRepository)
                .findById(userId);

        verify(oneCRestClient)
                .get();
    }


    @Test
    void getProductsFrom1C_shouldThrowExceptionWhenUserNotFound() {

        Long userId = 999L;

        when(usersRepository.findById(userId))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> productService.getProductsFrom1C(userId)
                );

        assertThat(exception.getMessage())
                .isEqualTo("User not found");

        verify(usersRepository)
                .findById(userId);

        verifyNoInteractions(oneCRestClient);
    }


//    @Test
//    void updateProductsFrom1C_shouldCreateNewProducts() {
//
//        ProductDto product1 = ProductDto.builder()
//                .productCode("P001")
//                .productName("Product 1")
//                .price(new BigDecimal("100.00"))
//                .stock(new BigDecimal("20"))
//                .build();
//
//        ProductDto product2 = ProductDto.builder()
//                .productCode("P002")
//                .productName("Product 2")
//                .price(new BigDecimal("250.00"))
//                .stock(new BigDecimal("5"))
//                .build();
//
//        when(oneCRestClient.get())
//                .thenReturn(requestHeadersUriSpec);
//
//        when(requestHeadersUriSpec.uri("/products"))
//                .thenReturn(requestHeadersSpec);
//
//        when(requestHeadersSpec.retrieve())
//                .thenReturn(responseSpec);
//
//        when(responseSpec.body(ProductDto[].class))
//                .thenReturn(new ProductDto[]{
//                        product1,
//                        product2
//                });
//
//        when(productRepository.findByProductCode("P001"))
//                .thenReturn(Optional.empty());
//
//        when(productRepository.findByProductCode("P002"))
//                .thenReturn(Optional.empty());
//
//        productService.updateProductsFrom1C();
//
//        ArgumentCaptor<Product> captor =
//                ArgumentCaptor.forClass(Product.class);
//
//        verify(productRepository, times(2))
//                .save(captor.capture());
//
//        List<Product> savedProducts =
//                captor.getAllValues();
//
//        assertThat(savedProducts)
//                .hasSize(2);
//
//        Product savedProduct1 = savedProducts.get(0);
//
//        assertThat(savedProduct1.getProductCode())
//                .isEqualTo("P001");
//
//        assertThat(savedProduct1.getProductName())
//                .isEqualTo("Product 1");
//
//        assertThat(savedProduct1.getPrice())
//                .isEqualByComparingTo("100.00");
//
//        assertThat(savedProduct1.getStock())
//                .isEqualByComparingTo("20");
//
//        assertThat(savedProduct1.isActive())
//                .isTrue();
//
//        Product savedProduct2 = savedProducts.get(1);
//
//        assertThat(savedProduct2.getProductCode())
//                .isEqualTo("P002");
//
//        assertThat(savedProduct2.getProductName())
//                .isEqualTo("Product 2");
//
//        assertThat(savedProduct2.getPrice())
//                .isEqualByComparingTo("250.00");
//
//        assertThat(savedProduct2.getStock())
//                .isEqualByComparingTo("5");
//
//        assertThat(savedProduct2.isActive())
//                .isTrue();
//    }
//
//
//    @Test
//    void updateProductsFrom1C_shouldUpdateExistingProduct() {
//
//        Product existingProduct = Product.builder()
//                .id(10L)
//                .productCode("P001")
//                .productName("Old name")
//                .price(new BigDecimal("50.00"))
//                .stock(new BigDecimal("5"))
//                .active(false)
//                .build();
//
//        ProductDto productFrom1C = ProductDto.builder()
//                .productCode("P001")
//                .productName("New name")
//                .price(new BigDecimal("150.00"))
//                .stock(new BigDecimal("20"))
//                .build();
//
//        when(oneCRestClient.get())
//                .thenReturn(requestHeadersUriSpec);
//
//        when(requestHeadersUriSpec.uri("/products"))
//                .thenReturn(requestHeadersSpec);
//
//        when(requestHeadersSpec.retrieve())
//                .thenReturn(responseSpec);
//
//        when(responseSpec.body(ProductDto[].class))
//                .thenReturn(new ProductDto[]{
//                        productFrom1C
//                });
//
//        when(productRepository.findByProductCode("P001"))
//                .thenReturn(Optional.of(existingProduct));
//
//        productService.updateProductsFrom1C();
//
//        verify(productRepository)
//                .save(existingProduct);
//
//        assertThat(existingProduct.getId())
//                .isEqualTo(10L);
//
//        assertThat(existingProduct.getProductCode())
//                .isEqualTo("P001");
//
//        assertThat(existingProduct.getProductName())
//                .isEqualTo("New name");
//
//        assertThat(existingProduct.getPrice())
//                .isEqualByComparingTo("150.00");
//
//        assertThat(existingProduct.getStock())
//                .isEqualByComparingTo("20");
//
//        assertThat(existingProduct.isActive())
//                .isTrue();
//    }
//
//
//    @Test
//    void updateProductsFrom1C_shouldDoNothingWhen1CReturnsNull() {
//
//        when(oneCRestClient.get())
//                .thenReturn(requestHeadersUriSpec);
//
//        when(requestHeadersUriSpec.uri("/products"))
//                .thenReturn(requestHeadersSpec);
//
//        when(requestHeadersSpec.retrieve())
//                .thenReturn(responseSpec);
//
//        when(responseSpec.body(ProductDto[].class))
//                .thenReturn(null);
//
//        productService.updateProductsFrom1C();
//
//        verify(productRepository, never())
//                .findByProductCode(any());
//
//        verify(productRepository, never())
//                .save(any(Product.class));
//    }
//
//
//    @Test
//    void getAllProducts_shouldReturnAllProducts() {
//
//        Product product1 = Product.builder()
//                .id(1L)
//                .productCode("P001")
//                .productName("Product 1")
//                .price(new BigDecimal("100"))
//                .stock(new BigDecimal("10"))
//                .active(true)
//                .build();
//
//        Product product2 = Product.builder()
//                .id(2L)
//                .productCode("P002")
//                .productName("Product 2")
//                .price(new BigDecimal("200"))
//                .stock(new BigDecimal("5"))
//                .active(true)
//                .build();
//
//        when(productRepository.findAll())
//                .thenReturn(List.of(product1, product2));
//
//        List<Product> result =
//                productService.getAllProducts();
//
//        assertThat(result)
//                .hasSize(2);
//
//        assertThat(result)
//                .containsExactly(product1, product2);
//
//        verify(productRepository)
//                .findAll();
//    }
//
//
//    @Test
//    void getAllProducts_shouldReturnEmptyListWhenNoProducts() {
//
//        when(productRepository.findAll())
//                .thenReturn(List.of());
//
//        List<Product> result =
//                productService.getAllProducts();
//
//        assertThat(result)
//                .isEmpty();
//
//        verify(productRepository)
//                .findAll();
//    }
//
//
//    @Test
//    void testConnection_shouldReturnResponseFrom1C() {
//
//        when(oneCRestClient.get())
//                .thenReturn(requestHeadersUriSpec);
//
//        when(requestHeadersUriSpec.uri("/test"))
//                .thenReturn(requestHeadersSpec);
//
//        when(requestHeadersSpec.retrieve())
//                .thenReturn(responseSpec);
//
//        when(responseSpec.body(String.class))
//                .thenReturn("OK");
//
//        String result =
//                productService.testConnection();
//
//        assertThat(result)
//                .isEqualTo("OK");
//
//        verify(oneCRestClient)
//                .get();
//
//        verify(requestHeadersUriSpec)
//                .uri("/test");
//
//        verify(responseSpec)
//                .body(String.class);
//    }
}
