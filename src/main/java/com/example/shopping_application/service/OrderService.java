package com.example.shopping_application.service;


import com.example.shopping_application.dto.orders.CreateOrderRequest;
import com.example.shopping_application.dto.orders.OrderItemRequest;
import com.example.shopping_application.dto.orders.OrderItemResponse;
import com.example.shopping_application.dto.orders.OrderResponse;
import com.example.shopping_application.entity.Order;
import com.example.shopping_application.entity.OrderItem;
import com.example.shopping_application.entity.User;
import com.example.shopping_application.enums.OrderStatus;
import com.example.shopping_application.repository.OrderRepository;
import com.example.shopping_application.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.shopping_application.enums.OrderStatus.CONFIRMED;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemService orderItemService;

    public OrderResponse createOrder(CreateOrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(CONFIRMED);

        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequest req : request.getItems()) {
            OrderItem item = orderItemService.buildOrderItem(
                    order,
                    req.getProductId(),
                    req.getQuantity()
            );
            items.add(item);
        }

        order.setOrderItemList(items);

        order.setTotalAmount(orderItemService.calculateTotal(items));

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> OrderItems = order.getOrderItemList().stream()
                .map(i -> new OrderItemResponse(
                        i.getProduct().getId(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getSubtotal()
                )).toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getUser().getId(),
                OrderItems
        );
    }
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        return toResponse(order);
    }

    // 3️⃣ GET ALL ORDERS (pagination + sorting)
    public Page<OrderResponse> getAllOrders(int page, int size, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return orderRepository.findAll(pageable)
                .map(this::toResponse);
    }

    // 4️⃣ UPDATE ONLY ORDER STATUS
    public void updateOrderStatus(Long orderId, String orderStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setOrderStatus(OrderStatus.valueOf(orderStatus));
        orderRepository.save(order);
    }

    // 5️⃣ DELETE ORDER
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        orderRepository.delete(order);
    }


}

/*
import com.example.shopping_application.dto.orders.CreateOrderRequest;
import com.example.shopping_application.dto.orders.OrderItemRequest;
import com.example.shopping_application.dto.orders.OrderItemResponse;
import com.example.shopping_application.dto.orders.OrderResponse;
import com.example.shopping_application.entity.Order;
import com.example.shopping_application.entity.OrderItem;
import com.example.shopping_application.entity.Product;
import com.example.shopping_application.entity.User;
import com.example.shopping_application.repository.OrderRepository;
import com.example.shopping_application.repository.ProductRepository;
import com.example.shopping_application.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderResponse createOrder(CreateOrderRequest createOrderRequest) {

        User user = userRepository.findById(createOrderRequest.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);

        List<OrderItem> orderItems = listOfOrderItems(order, createOrderRequest.getItems());
        order.setOrderItemList(orderItems);

        double totalAmount = calculateTotalAmount(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return toOrderResponse(savedOrder);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    private List<OrderItem> listOfOrderItems(Order order, List<OrderItemRequest> itemRequests) {

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest orderItemRequest : itemRequests) {
            Product product = getProduct(orderItemRequest.getProductId());

            validateStock(product, orderItemRequest.getQuantity());
            reduceStock(product, orderItemRequest.getQuantity());

            OrderItem item = createOrderItem(order, product, orderItemRequest.getQuantity());
            orderItems.add(item);
        }
        return orderItems;
    }

    private void validateStock(Product product, Integer quantity) {
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock: " + product.getProductName());
        }
    }

    private void reduceStock(Product product, Integer quantity) {
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

    }

    private OrderItem createOrderItem(Order order, Product product, Integer quantity) {

        OrderItem OrderItem = new OrderItem();

        OrderItem.setOrder(order);
        OrderItem.setProduct(product);
        OrderItem.setQuantity(quantity);
        OrderItem.setPrice(product.getPrice());

        double subtotal = quantity * product.getPrice();
        OrderItem.setSubtotal(subtotal);

        return OrderItem;
    }

    private double calculateTotalAmount(List<OrderItem> items) {
        return items.stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
    }

    private OrderResponse toOrderResponse(Order order) {

        List<OrderItemResponse> items = order.getOrderItemList().stream()
                .map(i -> new OrderItemResponse(
                        i.getProduct().getId(),
//                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getSubtotal()
                )).toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getUser().getId(),
                items
        );
    }

}
*/

