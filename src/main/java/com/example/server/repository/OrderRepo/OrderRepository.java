package com.example.server.repository.OrderRepo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.server.model.Order.Order;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByBuyerId(String buyerId);

    List<Order> findBySellerId(String sellerId);

    List<Order> findByType(Order.OrderType type);

    List<Order> findByStatus(Order.OrderStatus status);

    @Query("{'orderDate': {$gte: ?0, $lte: ?1}}")
    List<Order> findOrdersBetweenDates(LocalDateTime start, LocalDateTime end);

    List<Order> findByTypeAndStatus(Order.OrderType type, Order.OrderStatus status);

    @Query("{'type': 'GREENHOUSE', 'installationDetails.scheduledInstallationDate': {$gte: ?0}}")
    List<Order> findUpcomingGreenhouseInstallations(LocalDateTime fromDate);
}