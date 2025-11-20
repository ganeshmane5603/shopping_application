package com.example.shopping_application.dto.orders;

import com.example.shopping_application.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class OrderResponse {
    private Long orderId;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private OrderStatus orderStatus;

    private Long userId;
    private List<OrderItemResponse> items;
}
