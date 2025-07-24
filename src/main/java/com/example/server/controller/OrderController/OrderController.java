package com.example.server.controller.OrderController;

import com.example.server.model.Order.Order;
import com.example.server.model.Order.Order.OrderStatus;
import com.example.server.model.Order.Order.OrderType;
import com.example.server.service.OrderService.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Create a new order (SEED, HARVEST, or GREENHOUSE)
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    // Get order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // Get all orders for a buyer (farmer or customer)
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Order>> getOrdersByBuyer(@PathVariable String buyerId) {
        return ResponseEntity.ok(orderService.getOrdersByBuyer(buyerId));
    }

    // Get all orders for a seller (seed supplier or farmer)
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Order>> getOrdersBySeller(@PathVariable String sellerId) {
        return ResponseEntity.ok(orderService.getOrdersBySeller(sellerId));
    }

    // Get orders by type (SEED, HARVEST, GREENHOUSE)
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Order>> getOrdersByType(@PathVariable OrderType type) {
        return ResponseEntity.ok(orderService.getOrdersByType(type));
    }

    // Get orders by type and status
    @GetMapping("/type/{type}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByTypeAndStatus(
            @PathVariable OrderType type,
            @PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getOrdersByTypeAndStatus(type, status));
    }

    // Update order status
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status, notes));
    }

    // Update greenhouse installation details
    @PutMapping("/{orderId}/installation")
    public ResponseEntity<Order> updateGreenhouseInstallation(
            @PathVariable String orderId,
            @RequestBody Order.GreenhouseInstallationDetails installationDetails) {
        return ResponseEntity.ok(orderService.updateGreenhouseInstallation(orderId, installationDetails));
    }

    // Get upcoming greenhouse installations
    @GetMapping("/greenhouse/installations")
    public ResponseEntity<List<Order>> getUpcomingInstallations() {
        return ResponseEntity.ok(orderService.getUpcomingInstallations());
    }

    // Get orders within a date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(orderService.getOrdersBetweenDates(start, end));
    }
}