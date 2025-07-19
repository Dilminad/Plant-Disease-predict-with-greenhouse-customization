package com.example.server.model.Marketplace;

import java.time.LocalDate;

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
@Document(collection = "harvest_orders")
public class HarvestOrder {
    @Id
    private String id;
    private String buyerId; // Reference to User
    private String harvestId; // Reference to Harvest
    private double quantity;
    private double totalPrice;
    private LocalDate orderDate;
    private String deliveryOption; // PICKUP, DELIVERY
    private String deliveryAddress;
    private double deliveryFee;
    private String status; // PENDING, SHIPPED, DELIVERED
    // getters, setters
}