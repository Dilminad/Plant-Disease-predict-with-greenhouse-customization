package com.example.server.service.Seed_and_Harvest_ListingService;

import com.example.server.model.Products.SeedListing;
import com.example.server.repository.Seed_and_Harvest_Repo.SeedListingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SeedListingService {

    private final SeedListingRepository seedListingRepository;

    @Autowired
    public SeedListingService(SeedListingRepository seedListingRepository) {
        this.seedListingRepository = seedListingRepository;
    }

    public List<SeedListing> getAllSeedListings() {
        return seedListingRepository.findAll();
    }

    public Optional<SeedListing> getSeedListingById(String id) {
        return seedListingRepository.findById(id);
    }

    public List<SeedListing> getSeedListingsBySellerId(String sellerId) {
        return seedListingRepository.findBySellerId(sellerId);
    }

    public List<SeedListing> getSeedListingsByCategory(SeedListing.SeedCategory category) {
        return seedListingRepository.findByCategory(category);
    }

    public List<SeedListing> searchSeedListingsByName(String name) {
        return seedListingRepository.findByNameContainingIgnoreCase(name);
    }

    public SeedListing createSeedListing(SeedListing seedListing) {
        seedListing.setListingDate(LocalDate.now());
        return seedListingRepository.save(seedListing);
    }

    public SeedListing updateSeedListing(String id, SeedListing updatedSeedListing) {
        return seedListingRepository.findById(id)
                .map(existingListing -> {
                    existingListing.setName(updatedSeedListing.getName());
                    existingListing.setDescription(updatedSeedListing.getDescription());
                    existingListing.setCategory(updatedSeedListing.getCategory());
                    existingListing.setPricePerUnit(updatedSeedListing.getPricePerUnit());
                    existingListing.setAvailableQuantity(updatedSeedListing.getAvailableQuantity());
                    existingListing.setImageUrl(updatedSeedListing.getImageUrl());
                    existingListing.setGerminationRate(updatedSeedListing.getGerminationRate());
                    existingListing.setDaysToMaturity(updatedSeedListing.getDaysToMaturity());
                    existingListing.setSellerRating(updatedSeedListing.getSellerRating());
                    return seedListingRepository.save(existingListing);
                })
                .orElseGet(() -> {
                    updatedSeedListing.setId(id);
                    return seedListingRepository.save(updatedSeedListing);
                });
    }

    public void deleteSeedListing(String id) {
        seedListingRepository.deleteById(id);
    }
}