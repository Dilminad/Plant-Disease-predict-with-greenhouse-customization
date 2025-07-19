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
@Document(collection = "seed_orders")
public class SeedOrder {
    @Id
    private String id;
    private String farmerId; // Reference to User
    private String seedId; // Reference to Seed
    private int quantity;
    private double totalPrice;
    private LocalDate orderDate;
    private String status; // PENDING, SHIPPED, DELIVERED
    // getters, setters
}