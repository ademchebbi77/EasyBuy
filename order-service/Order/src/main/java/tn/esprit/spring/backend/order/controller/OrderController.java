package tn.esprit.spring.backend.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.backend.order.dto.CreateOrderRequest;
import tn.esprit.spring.backend.order.dto.OrderResponse;
import tn.esprit.spring.backend.order.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id,
                                                     @RequestBody CreateOrderRequest request) {
        OrderResponse updatedOrder = orderService.updateOrder(id, request);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable Long id) {
        OrderResponse order = orderService.confirmOrder(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        OrderResponse order = orderService.cancelOrder(id);
        return ResponseEntity.ok(order);
    }
}