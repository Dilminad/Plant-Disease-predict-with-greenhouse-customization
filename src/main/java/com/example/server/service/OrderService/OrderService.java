package com.example.server.service.OrderService;


import com.example.server.model.Order.Order;
import com.example.server.model.Order.Order.OrderStatus;
import com.example.server.model.Order.Order.OrderType;
import com.example.server.model.Order.Order.PaymentStatus;
import com.example.server.repository.OrderRepo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // Create a new order (SEED, HARVEST, or GREENHOUSE)
    public Order createOrder(Order order) {
        // Generate unique order number with type and year
        order.setOrderNumber(generateOrderNumber(order.getType()));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

        // Calculate total price
        order.setTotalPrice(calculateTotalPrice(order));

        // Initialize status history
        Order.StatusHistory initialStatus = new Order.StatusHistory(
            OrderStatus.PENDING,
            LocalDateTime.now(),
            "Order created"
        );
        order.setStatusHistory(List.of(initialStatus));

        return orderRepository.save(order);
    }

    // Get order by ID
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    // Update order status (e.g., PENDING → DELIVERED)
    public Order updateOrderStatus(String orderId, OrderStatus newStatus, String notes) {
        Order order = getOrderById(orderId); // Use the new method

        // Add to status history
        Order.StatusHistory historyEntry = new Order.StatusHistory(
            newStatus,
            LocalDateTime.now(),
            notes
        );
        order.getStatusHistory().add(historyEntry);

        order.setStatus(newStatus);

        // Update actual delivery date if delivered
        if (newStatus == OrderStatus.DELIVERED) {
            order.setActualDeliveryDate(LocalDateTime.now());
        }

        return orderRepository.save(order);
    }

    // Update greenhouse installation details
    public Order updateGreenhouseInstallation(String orderId, Order.GreenhouseInstallationDetails installationDetails) {
        Order order = getOrderById(orderId); // Use the new method

        if (order.getType() != OrderType.GREENHOUSE) {
            throw new RuntimeException("Installation details can only be updated for GREENHOUSE orders");
        }

        order.setInstallationDetails(installationDetails);
        return orderRepository.save(order);
    }

    // Get all orders for a buyer (farmer or customer)
    public List<Order> getOrdersByBuyer(String buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    // Get all orders for a seller (seed supplier or farmer)
    public List<Order> getOrdersBySeller(String sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    // Get orders by type (SEED, HARVEST, GREENHOUSE)
    public List<Order> getOrdersByType(OrderType type) {
        return orderRepository.findByType(type);
    }

    // Get orders by type and status
    public List<Order> getOrdersByTypeAndStatus(OrderType type, OrderStatus status) {
        return orderRepository.findByTypeAndStatus(type, status);
    }

    // Get upcoming greenhouse installations
    public List<Order> getUpcomingInstallations() {
        return orderRepository.findUpcomingGreenhouseInstallations(LocalDateTime.now());
    }

    // Get orders within a date range
    public List<Order> getOrdersBetweenDates(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findOrdersBetweenDates(start, end);
    }

    // Helper method to generate order number
    private String generateOrderNumber(OrderType type) {
        return String.format("%s-%d-%s",
            type.name(),
            LocalDateTime.now().getYear(),
            UUID.randomUUID().toString().substring(0, 8).toUpperCase()
        );
    }

    // Helper method to calculate total price
    private double calculateTotalPrice(Order order) {
        return (order.getQuantity() * order.getUnitPrice()) +
               order.getPlatformFee() +
               order.getDeliveryFee();
    }
}