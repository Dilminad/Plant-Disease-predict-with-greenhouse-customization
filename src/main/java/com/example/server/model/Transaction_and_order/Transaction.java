package com.example.server.model.Transaction_and_order;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private String orderId;
    private String payerId;
    private String receiverId;
    private double amount;
    private double platformFee;
    private LocalDateTime transactionDate;
    private TransactionStatus status;
    private String paymentGatewayReference;
    
    public enum TransactionStatus { PENDING, COMPLETED, FAILED, REFUNDED }
}