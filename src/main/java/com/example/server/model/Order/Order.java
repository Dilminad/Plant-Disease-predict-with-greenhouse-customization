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
    private String orderNumber;
    private LocalDateTime orderDate;
    private String buyerId;
    private String sellerId;
    private OrderType type;
    private String itemId;
    private String itemName;
    private double quantity;
    private String unit;
    private double unitPrice;
    private double totalPrice;
    private double platformFee;
    private double sellerReceivableAmount; // New field for seller's net amount
    private double deliveryFee;
    private DeliveryOption deliveryOption;
    private Address deliveryAddress;
    private OrderStatus status;
    private List<StatusHistory> statusHistory;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private GreenhouseInstallationDetails installationDetails;

    // Enums and nested classes remain unchanged
    public enum OrderType { SEED, HARVEST, GREENHOUSE }
    public enum OrderStatus { PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED, REFUNDED }
    public enum PaymentMethod { CREDIT_CARD, BANK_TRANSFER, MOBILE_PAYMENT, CASH_ON_DELIVERY }
    public enum PaymentStatus { PENDING, AUTHORIZED, PAID, FAILED, REFUNDED }
    public enum DeliveryOption { PICKUP, DELIVERY }

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
        private String installationStatus;
    }
}