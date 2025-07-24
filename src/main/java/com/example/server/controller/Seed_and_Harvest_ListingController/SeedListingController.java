package com.example.server.controller.Seed_and_Harvest_ListingController;

import com.example.server.model.Products.SeedListing;
import com.example.server.service.Seed_and_Harvest_ListingService.SeedListingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seed-listings")
public class SeedListingController {

    private final SeedListingService seedListingService;

    @Autowired
    public SeedListingController(SeedListingService seedListingService) {
        this.seedListingService = seedListingService;
    }

    @GetMapping
    public ResponseEntity<List<SeedListing>> getAllSeedListings() {
        return ResponseEntity.ok(seedListingService.getAllSeedListings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeedListing> getSeedListingById(@PathVariable String id) {
        return seedListingService.getSeedListingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<SeedListing>> getSeedListingsBySellerId(@PathVariable String sellerId) {
        return ResponseEntity.ok(seedListingService.getSeedListingsBySellerId(sellerId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SeedListing>> getSeedListingsByCategory(
            @PathVariable SeedListing.SeedCategory category) {
        return ResponseEntity.ok(seedListingService.getSeedListingsByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SeedListing>> searchSeedListingsByName(
            @RequestParam String name) {
        return ResponseEntity.ok(seedListingService.searchSeedListingsByName(name));
    }

    @PostMapping
    public ResponseEntity<SeedListing> createSeedListing(@RequestBody SeedListing seedListing) {
        return ResponseEntity.ok(seedListingService.createSeedListing(seedListing));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeedListing> updateSeedListing(
            @PathVariable String id, @RequestBody SeedListing updatedSeedListing) {
        return ResponseEntity.ok(seedListingService.updateSeedListing(id, updatedSeedListing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeedListing(@PathVariable String id) {
        seedListingService.deleteSeedListing(id);
        return ResponseEntity.noContent().build();
    }
}