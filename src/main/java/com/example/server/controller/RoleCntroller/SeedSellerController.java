package com.example.server.controller.RoleCntroller;

import com.example.server.model.Roles.SeedSeller;
import com.example.server.service.RolesServices.SeedSellerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class SeedSellerController {

    @Autowired
    private SeedSellerService seedSellerService;

    @PostMapping("/auth/registerseedseller")
    public ResponseEntity<SeedSeller> registerSeedSeller(@RequestBody SeedSeller seedSeller) {
        return ResponseEntity.ok(seedSellerService.registerSeedSeller(seedSeller));
    }

    @GetMapping("/admin/allsellers")
    public ResponseEntity<List<SeedSeller>> getAllSeedSellers() {
        return ResponseEntity.ok(seedSellerService.getAllSeedSellers());
    }

    @GetMapping("/getseller/{id}")
    public ResponseEntity<SeedSeller> getSeedSellerById(@PathVariable String id) {
        return ResponseEntity.ok(seedSellerService.getSeedSellerById(id));
    }

    @PutMapping("/buyer/update/{id}")
    public ResponseEntity<SeedSeller> updateSeedSeller(@PathVariable String id, @RequestBody SeedSeller sellerDetails) {
        return ResponseEntity.ok(seedSellerService.updateSeedSeller(id, sellerDetails));
    }

    @PutMapping("/{id}/rating/{rating}")
    public ResponseEntity<SeedSeller> updateSellerRating(
            @PathVariable String id,
            @PathVariable double rating) {
        return ResponseEntity.ok(seedSellerService.updateSellerRating(id, rating));
    }

    @GetMapping("/top-rated/{limit}")
    public ResponseEntity<List<SeedSeller>> getTopRatedSellers(@PathVariable int limit) {
        return ResponseEntity.ok(seedSellerService.getTopRatedSellers(limit));
    }

    @GetMapping("/seed/{seedId}")
    public ResponseEntity<List<SeedSeller>> getSellersWithSeed(@PathVariable String seedId) {
        return ResponseEntity.ok(seedSellerService.getSellersWithSeed(seedId));
    }

    @GetMapping("/premium")
    public ResponseEntity<List<SeedSeller>> getPremiumSellers(
            @RequestParam double minRating,
            @RequestParam int minYears) {
        return ResponseEntity.ok(seedSellerService.getPremiumSellers(minRating, minYears));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeedSeller(@PathVariable String id) {
        seedSellerService.deleteSeedSeller(id);
        return ResponseEntity.noContent().build();
    }
}
