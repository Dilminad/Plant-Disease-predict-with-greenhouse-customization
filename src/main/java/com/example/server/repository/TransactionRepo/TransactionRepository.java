package com.example.server.repository.TransactionRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.Transaction_and_order.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByPayerId(String payerId);

    List<Transaction> findByReceiverId(String receiverId);

    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    List<Transaction> findByOrderId(String orderId);
}