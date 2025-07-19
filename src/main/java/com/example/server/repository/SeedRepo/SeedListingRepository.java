package com.example.server.repository.SeedRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.Products.SeedListing;

public interface SeedListingRepository extends MongoRepository<SeedListing, String> {
    List<SeedListing> findBySellerId(String sellerId);

    List<SeedListing> findByCategory(SeedListing.SeedCategory category);

    List<SeedListing> findByNameContainingIgnoreCase(String name);
}