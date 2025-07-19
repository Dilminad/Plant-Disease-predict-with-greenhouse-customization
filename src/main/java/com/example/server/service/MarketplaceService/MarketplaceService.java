package com.example.server.service.MarketplaceService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.model.Products.Harvest;
import com.example.server.model.Products.SeedListing;
import com.example.server.repository.HarvestRepo.HarvestRepository;
import com.example.server.repository.SeedRepo.SeedListingRepository;

@Service
public class MarketplaceService {
   @Autowired
    private HarvestRepository harvestRepository;
    
    @Autowired
    private SeedListingRepository seedListingRepository;

    public List<Harvest> getAllHarvests() {
        return harvestRepository.findAll();
    }

    public List<Harvest> searchHarvests(String query) {
        return harvestRepository.findByProductNameContainingIgnoreCase(query);
    }

    public Harvest createHarvestListing(Harvest harvest) {
        return harvestRepository.save(harvest);
    }

    public List<SeedListing> getAllSeedListings() {
        return seedListingRepository.findAll();
    }

    public List<SeedListing> searchSeedListings(String query) {
        return seedListingRepository.findByNameContainingIgnoreCase(query);
    }

    public SeedListing createSeedListing(SeedListing seedListing) {
        return seedListingRepository.save(seedListing);
    }
}
