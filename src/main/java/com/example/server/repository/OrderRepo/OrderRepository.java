package com.example.server.repository.OrderRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.Transaction_and_order.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByBuyerId(String buyerId);

    List<Order> findByStatus(Order.OrderStatus status);

    List<Order> findByType(Order.OrderType type);
}