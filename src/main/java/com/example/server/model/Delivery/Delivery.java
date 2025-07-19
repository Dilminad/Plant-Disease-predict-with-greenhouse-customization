package com.example.server.model.Delivery;

import java.time.LocalDateTime;
import java.util.List;

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
@Document(collection = "deliveries")
public class Delivery {  // Changed from 'deliveries' to 'Delivery'
    @Id
    private String id;
    private String orderId;
    private String assignedDriverId;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
    private DeliveryStatus status;
    private String trackingNumber;
    private List<LocationUpdate> routeHistory;
    private String vehicleType;
    private double deliveryFee;
    
    public enum DeliveryStatus {
        PENDING, 
        PICKED_UP, 
        IN_TRANSIT, 
        DELIVERED, 
        FAILED
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationUpdate {
        private double latitude;
        private double longitude;
        private LocalDateTime timestamp;
        private String status;
    }
}