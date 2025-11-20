package com.example.shopping_application.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OrderItemResponse {
    private Long productId;
//    private String productName;
    private Integer quantity;
    private Double price;
    private Double subtotal;
}
