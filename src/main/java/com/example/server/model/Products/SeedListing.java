package com.example.server.model.Products;

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
@Document(collection = "seed_listings")
public class SeedListing {
    @Id
    private String id;
    private String sellerId;
    private String name;
    private String description;
    private SeedCategory category;
    private double pricePerUnit;
    private int availableQuantity;
    private String imageUrl;
    private double germinationRate;
    private int daysToMaturity;
    private LocalDate listingDate;
    private double sellerRating;
    
    public enum SeedCategory {
        VEGETABLE, FRUIT, GRAIN, HERB, FLOWER
    }
}