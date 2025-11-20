package com.example.shopping_application.dto.orders;

import com.example.shopping_application.entity.User;
import jdk.dynalink.linker.LinkerServices;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;
}
