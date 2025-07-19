package com.example.server.service.OrderService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.server.model.Transaction_and_order.Order;
import com.example.server.model.Transaction_and_order.Transaction;
import com.example.server.repository.OrderRepo.OrderRepository;
import com.example.server.repository.TransactionRepo.TransactionRepository;

public class OrderService {
    
 @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    public Order updateOrderStatus(String orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    public Transaction createTransaction(Transaction transaction) {
        transaction.setTransactionDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransactionStatus(String transactionId, Transaction.TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction != null) {
            transaction.setStatus(status);
            return transactionRepository.save(transaction);
        }
        return null;
    }
}