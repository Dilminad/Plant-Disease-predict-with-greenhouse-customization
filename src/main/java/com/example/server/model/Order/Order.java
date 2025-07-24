package com.example.server.model.Order;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String orderNumber; // Auto-generated (e.g., "ORD-2023-12345")
    private LocalDateTime orderDate;
    
    // Parties involved
    private String buyerId;    // Farmer (for SEED/GREENHOUSE) or Buyer (for HARVEST)
    private String sellerId;   // Seed Supplier (for SEED) or Farmer (for HARVEST)
    
    // Order type & item details
    private OrderType type;    // SEED, HARVEST, GREENHOUSE
    private String itemId;     // ID of Seed/Harvest/GreenhouseModel
    private String itemName;   // For display (e.g., "Tomato Seeds - Organic")
    private double quantity;
    private String unit;       // kg, packets, units, etc.
    
    // Pricing
    private double unitPrice;
    private double totalPrice;
    private double platformFee;
    private double deliveryFee;
    
    // Delivery
    private DeliveryOption deliveryOption; // PICKUP, DELIVERY
    private Address deliveryAddress;      // Structured address
    
    // Status tracking
    private OrderStatus status;
    private List<StatusHistory> statusHistory; // Audit trail
    
    // Payment
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;         // Payment gateway reference
    
    // Timestamps
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    
    // Only for GREENHOUSE orders
    private GreenhouseInstallationDetails installationDetails;

    // ===== ENUMS =====
    public enum OrderType { SEED, HARVEST, GREENHOUSE }

    public enum OrderStatus {
        PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, 
        CANCELLED, RETURNED, REFUNDED
    }

    public enum PaymentMethod {
        CREDIT_CARD, BANK_TRANSFER, MOBILE_PAYMENT, CASH_ON_DELIVERY
    }

    public enum PaymentStatus {
        PENDING, AUTHORIZED, PAID, FAILED, REFUNDED
    }

    public enum DeliveryOption { PICKUP, DELIVERY }

    // ===== NESTED CLASSES =====
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String country;
        private String contactPhone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusHistory {
        private OrderStatus status;
        private LocalDateTime timestamp;
        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GreenhouseInstallationDetails {
        private LocalDate scheduledInstallationDate;
        private String installationTeam;
        private String installationNotes;
        private String installationStatus; // PLANNED, IN_PROGRESS, COMPLETED
    }
}