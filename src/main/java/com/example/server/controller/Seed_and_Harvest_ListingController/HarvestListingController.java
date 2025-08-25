package com.example.server.controller.Seed_and_Harvest_ListingController;

import com.example.server.model.Products.Harvest;
import com.example.server.service.Seed_and_Harvest_ListingService.HarvestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HarvestListingController {

    private final HarvestService harvestService;

    @Autowired
    public HarvestListingController(HarvestService harvestService) {
        this.harvestService = harvestService;
    }

    @GetMapping("auth/allproducts")
    public ResponseEntity<List<Harvest>> getAllHarvests() {
        return ResponseEntity.ok(harvestService.getAllHarvests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Harvest> getHarvestById(@PathVariable String id) {
        return harvestService.getHarvestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Harvest>> getHarvestsByFarmerId(@PathVariable String farmerId) {
        return ResponseEntity.ok(harvestService.getHarvestsByFarmerId(farmerId));
    }

    @GetMapping("/search/productname")
    public ResponseEntity<List<Harvest>> searchHarvestsByProductName(
            @RequestParam String productName) {
        return ResponseEntity.ok(harvestService.searchHarvestsByProductName(productName));
    }

    @GetMapping("/organic/{status}")
    public ResponseEntity<List<Harvest>> getHarvestsByOrganicStatus(
            @PathVariable Harvest.OrganicStatus status) {
        return ResponseEntity.ok(harvestService.getHarvestsByOrganicStatus(status));
    }

    @GetMapping("/quantity")
    public ResponseEntity<List<Harvest>> getHarvestsWithMinQuantity(
            @RequestParam double quantity) {
        return ResponseEntity.ok(harvestService.getHarvestsWithMinQuantity(quantity));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Harvest>> getHarvestsInPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {
        return ResponseEntity.ok(harvestService.getHarvestsInPriceRange(minPrice, maxPrice));
    }

    @GetMapping("/recent/products")
    public ResponseEntity<List<Harvest>> getRecentHarvests(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(harvestService.getRecentHarvests(date));
    }

    @GetMapping("/non-expired")
    public ResponseEntity<List<Harvest>> getNonExpiredHarvests(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentDate) {
        return ResponseEntity.ok(harvestService.getNonExpiredHarvests(currentDate));
    }

    @GetMapping("/auth/organic-price-range")
    public ResponseEntity<List<Harvest>> getOrganicHarvestsInPriceRange(
            @RequestParam Harvest.OrganicStatus status,
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {
        return ResponseEntity.ok(harvestService.getOrganicHarvestsInPriceRange(
                status, minPrice, maxPrice));
    }

    @PostMapping("/farmer/listharvest")
    public ResponseEntity<Harvest> createHarvest(@RequestBody Harvest harvest) {
        return ResponseEntity.ok(harvestService.createHarvest(harvest));
    }

    @PutMapping("/farmer/updateharvest/{id}")
    public ResponseEntity<Harvest> updateHarvest(
            @PathVariable String id, @RequestBody Harvest updatedHarvest) {
        return ResponseEntity.ok(harvestService.updateHarvest(id, updatedHarvest));
    }

    @DeleteMapping("/auth/delete/{id}")
    public ResponseEntity<Void> deleteHarvest(@PathVariable String id) {
        harvestService.deleteHarvest(id);
        return ResponseEntity.noContent().build();
    }

    // --- NEW ENDPOINT ADDED ---
    // This endpoint allows the frontend to fetch products for a farmer that are expiring soon.
    @GetMapping("/farmer/{farmerId}/expiring-soon")
    public ResponseEntity<List<Harvest>> getExpiringSoonProducts(
            @PathVariable String farmerId,
            @RequestParam(defaultValue = "7") int days) { // The frontend can specify the number of days, defaulting to 7.
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(days);
        return ResponseEntity.ok(harvestService.getExpiringSoonProductsForFarmer(farmerId, today, futureDate));
    }
}