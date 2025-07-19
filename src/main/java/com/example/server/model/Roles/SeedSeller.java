package com.example.server.model.Roles;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "seed_sellers")
public class SeedSeller extends User {
    private String businessLicense;
    private double sellerRating;
    private int yearsInBusiness;
     private List<String> seeds;
    
    @Override
    public String getRole() { 
        return "SEED_SELLER"; 
    }
}