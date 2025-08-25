package com.example.server.model.Products;

import java.time.LocalDate;
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
@Document(collection = "harvest_listings")
public class Harvest {
    @Id
    private String id;
    private String farmerId;
    private String productName;
    private String description;
    private double quantityAvailable;
    private String unit;
    private double pricePerUnit;
    private LocalDate harvestDate;
    private LocalDate expiryDate;
    private OrganicStatus organicStatus;
    private List<String> certifications;
    private List<String> imageUrls;

    
    public enum OrganicStatus {
        CERTIFIED_ORGANIC, NON_ORGANIC, TRANSITIONAL
    }
}