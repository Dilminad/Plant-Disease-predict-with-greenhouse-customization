package com.example.server.repository.OrderRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.server.model.Order.Order;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    // Find orders by buyer (farmer or customer)
    List<Order> findByBuyerId(String buyerId);

    // Find orders by seller (seed supplier or farmer)
    List<Order> findBySellerId(String sellerId);

    // Find orders by type (SEED, HARVEST, GREENHOUSE)
    List<Order> findByType(Order.OrderType type);

    // Find orders by status (PENDING, DELIVERED, etc.)
    List<Order> findByStatus(Order.OrderStatus status);

    // Find orders within a date range
    @Query("{'orderDate': {$gte: ?0, $lte: ?1}}")
    List<Order> findOrdersBetweenDates(LocalDateTime start, LocalDateTime end);

    // Find orders by type and status (e.g., "all DELIVERED HARVEST orders")
    List<Order> findByTypeAndStatus(Order.OrderType type, Order.OrderStatus status);

    // Find GREENHOUSE orders with scheduled installations
    @Query("{'type': 'GREENHOUSE', 'installationDetails.scheduledInstallationDate': {$gte: ?0}}")
    List<Order> findUpcomingGreenhouseInstallations(LocalDateTime fromDate);

    
}