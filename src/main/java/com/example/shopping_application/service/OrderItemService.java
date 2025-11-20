package com.example.shopping_application.service;

import com.example.shopping_application.entity.Order;
import com.example.shopping_application.entity.OrderItem;
import com.example.shopping_application.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final ProductService productService;

    public OrderItem buildOrderItem(Order order, Long productId, int quantity) {

        Product product = productService.getProduct(productId);

        productService.validateStock(product, quantity);
        productService.reduceStock(product, quantity);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());
        item.setSubtotal(product.getPrice() * quantity);

        return item;
    }

    public double calculateTotal(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }
}
