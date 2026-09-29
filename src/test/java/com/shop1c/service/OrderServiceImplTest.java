package com.shop1c.service;

import com.shop1c.entity.Order;
import com.shop1c.entity.Users;
import com.shop1c.entity.dto.OrderItemRequestDto;
import com.shop1c.entity.dto.OrderRequestDto;
import com.shop1c.entity.dto.OrderResponseDto;
import com.shop1c.repository.OrderRepository;
import com.shop1c.repository.UsersRepository;
import com.shop1c.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_shouldCalculateTotalForMultipleItems() {

        Long userId = 1L;

        Users user = Users.builder()
                .id(userId)
                .code1C("CUST001")
                .name1C("Customer")
                .build();

        OrderItemRequestDto item1 = OrderItemRequestDto.builder()
                .productId(1L)
                .productCode("P001")
                .productName("Product 1")
                .quantity(new BigDecimal("2"))
                .price(new BigDecimal("100"))
                .build();

        OrderItemRequestDto item2 = OrderItemRequestDto.builder()
                .productId(2L)
                .productCode("P002")
                .productName("Product 2")
                .quantity(new BigDecimal("3"))
                .price(new BigDecimal("50"))
                .build();

        OrderRequestDto request = OrderRequestDto.builder()
                .items(List.of(item1, item2))
                .build();

        when(usersRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDto result =
                orderService.createOrder(userId, request);

        assertThat(result.getTotalAmount())
                .isEqualByComparingTo("350");

        assertThat(result.getItems())
                .hasSize(2);

        assertThat(result.getItems().get(0).getAmount())
                .isEqualByComparingTo("200");

        assertThat(result.getItems().get(1).getAmount())
                .isEqualByComparingTo("150");
    }

    @Test
    void getMyOrders_shouldReturnCustomerOrders() {

        Long customerId = 1L;

        Order order = Order.builder()
                .id(10L)
                .customerId(customerId)
                .customerCode("CUST001")
                .customerName("Customer")
                .totalAmount(new BigDecimal("150"))
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        when(orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId))
                .thenReturn(List.of(order));

        List<OrderResponseDto> result =
                orderService.getMyOrders(customerId);

        assertThat(result)
                .hasSize(1);

        assertThat(result.getFirst().getId())
                .isEqualTo(10L);

        assertThat(result.getFirst().getCustomerId())
                .isEqualTo(customerId);

        assertThat(result.getFirst().getCustomerCode())
                .isEqualTo("CUST001");

        assertThat(result.getFirst().getTotalAmount())
                .isEqualByComparingTo("150");

        verify(orderRepository)
                .findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Test
    void getMyOrders_shouldReturnEmptyList_whenNoOrders() {

        Long customerId = 1L;

        when(orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId))
                .thenReturn(List.of());

        List<OrderResponseDto> result =
                orderService.getMyOrders(customerId);

        assertThat(result)
                .isEmpty();

        verify(orderRepository)
                .findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Test
    void getAllOrders_shouldReturnAllOrders() {

        Order order1 = Order.builder()
                .id(1L)
                .customerId(10L)
                .customerCode("CUST001")
                .customerName("Customer 1")
                .totalAmount(new BigDecimal("100"))
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        Order order2 = Order.builder()
                .id(2L)
                .customerId(20L)
                .customerCode("CUST002")
                .customerName("Customer 2")
                .totalAmount(new BigDecimal("200"))
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        when(orderRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(order1, order2));

        List<OrderResponseDto> result =
                orderService.getAllOrders();

        assertThat(result)
                .hasSize(2);

        assertThat(result.get(0).getId())
                .isEqualTo(1L);

        assertThat(result.get(1).getId())
                .isEqualTo(2L);

        verify(orderRepository)
                .findAllByOrderByCreatedAtDesc();
    }

    @Test
    void createOrder_shouldCreateOrderSuccessfully() {

        Long userId = 1L;

        Users user = Users.builder()
                .id(userId)
                .code1C("CUST001")
                .name1C("Test Customer")
                .build();

        OrderItemRequestDto item = OrderItemRequestDto.builder()
                .productId(100L)
                .productCode("P001")
                .productName("Product 1")
                .quantity(new BigDecimal("2"))
                .price(new BigDecimal("50.00"))
                .build();

        OrderRequestDto request = OrderRequestDto.builder()
                .items(List.of(item))
                .build();

        when(usersRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDto result =
                orderService.createOrder(userId, request);

        assertThat(result).isNotNull();

        assertThat(result.getCustomerId())
                .isEqualTo(userId);

        assertThat(result.getCustomerCode())
                .isEqualTo("CUST001");

        assertThat(result.getCustomerName())
                .isEqualTo("Test Customer");

        assertThat(result.getTotalAmount())
                .isEqualByComparingTo("100.00");

        assertThat(result.getItems())
                .hasSize(1);

        assertThat(result.getItems().get(0).getAmount())
                .isEqualByComparingTo("100.00");

        verify(usersRepository)
                .findById(userId);

        verify(orderRepository)
                .save(any(Order.class));
    }

    @Test
    void createOrder_shouldThrowException_whenUserNotFound() {

        Long userId = 999L;

        Order order1 = Order.builder()
                .id(1L)
                .customerId(10L)
                .customerCode("CUST001")
                .customerName("Customer 1")
                .totalAmount(new BigDecimal("100"))
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        when(usersRepository.findById(userId))
                .thenReturn(Optional.empty());

        OrderRequestDto request = OrderRequestDto.builder()
                .items(List.of())
                .build();

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.createOrder(userId, request)
        );

        assertThat(exception.getMessage())
                .isEqualTo("User not found");

        verify(usersRepository)
                .findById(userId);

        verify(orderRepository, never())
                .save(order1);
    }
}
