package com.example.server.controller.Seed_and_Harvest_ListingController;

import com.example.server.model.Products.SeedListing;
import com.example.server.service.Seed_and_Harvest_ListingService.SeedListingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SeedListingController {

    private final SeedListingService seedListingService;

    @Autowired
    public SeedListingController(SeedListingService seedListingService) {
        this.seedListingService = seedListingService;
    }

    @GetMapping("/auth/allseeds")
    public ResponseEntity<List<SeedListing>> getAllSeedListings() {
        return ResponseEntity.ok(seedListingService.getAllSeedListings());
    }
    
    @GetMapping("/auth/seeds/{id}")
    public ResponseEntity<SeedListing> getSeedListingById(@PathVariable String id) {
        return seedListingService.getSeedListingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/seed-seller/{sellerId}")
    public ResponseEntity<List<SeedListing>> getSeedListingsBySellerId(@PathVariable String sellerId) {
        System.out.println("Fetching listings for sellerId: " + sellerId);
        return ResponseEntity.ok(seedListingService.getSeedListingsBySellerId(sellerId));
    }

    @GetMapping("/auth/category/{category}")
    public ResponseEntity<List<SeedListing>> getSeedListingsByCategory(
            @PathVariable SeedListing.SeedCategory category) {
        return ResponseEntity.ok(seedListingService.getSeedListingsByCategory(category));
    }

    @GetMapping("/auth/seeds/search")
    public ResponseEntity<List<SeedListing>> searchSeedListingsByName(
            @RequestParam String name) {
        return ResponseEntity.ok(seedListingService.searchSeedListingsByName(name));
    }

    @PostMapping("/seed-seller/createnew")
    public ResponseEntity<SeedListing> createSeedListing(@RequestBody SeedListing seedListing) {
        return ResponseEntity.ok(seedListingService.createSeedListing(seedListing));
    }

    @PutMapping("/seed-seller/update/{id}")
    public ResponseEntity<SeedListing> updateSeedListing(
            @PathVariable String id, @RequestBody SeedListing updatedSeedListing) {
        return ResponseEntity.ok(seedListingService.updateSeedListing(id, updatedSeedListing));
    }

    @DeleteMapping("/seed-seller/delete/{id}")
    public ResponseEntity<Void> deleteSeedListing(@PathVariable String id) {
        seedListingService.deleteSeedListing(id);
        return ResponseEntity.noContent().build();
    }
    
    // ++ ADD ENDPOINT TO UPDATE STOCK ++
    @PatchMapping("/seed-seller/update-stock/{id}")
    public ResponseEntity<SeedListing> updateStockQuantity(
            @PathVariable String id, 
            @RequestParam int quantityChange) {
        try {
            SeedListing updatedListing = seedListingService.updateStockQuantity(id, quantityChange);
            return ResponseEntity.ok(updatedListing);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}