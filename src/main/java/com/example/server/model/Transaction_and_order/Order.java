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
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String buyerId;
    private OrderType type; // SEED, HARVEST
    private String itemId; // SeedListing or HarvestListing ID
    private double quantity;
    private double unitPrice;
    private double totalPrice;
    private double platformFee;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private String deliveryOption; // PICKUP, DELIVERY
    private String deliveryAddress;
    private LocalDateTime deliveryDate;
    
    public enum OrderType { SEED, HARVEST }
    public enum OrderStatus { PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED }
    public enum PaymentMethod { CREDIT_CARD, BANK_TRANSFER, MOBILE_PAYMENT }
}