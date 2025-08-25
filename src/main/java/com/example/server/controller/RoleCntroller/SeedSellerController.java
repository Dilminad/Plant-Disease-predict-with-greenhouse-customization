package com.example.server.controller.RoleCntroller;

import com.example.server.model.Roles.SeedSeller;
import com.example.server.service.RolesServices.SeedSellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SeedSellerController {

    @Autowired
    private SeedSellerService seedSellerService;

    @PostMapping("/auth/registerseedseller")
    public ResponseEntity<SeedSeller> registerSeedSeller(@RequestBody SeedSeller seedSeller) {
        return ResponseEntity.ok(seedSellerService.registerSeedSeller(seedSeller));
    }

    @GetMapping("/admin/allseedsellers")
    public ResponseEntity<List<SeedSeller>> getAllSeedSellers() {
        return ResponseEntity.ok(seedSellerService.getAllSeedSellers());
    }

    @GetMapping("/seedseller/{id}")
    public ResponseEntity<SeedSeller> getSeedSellerById(@PathVariable String id) {
        return ResponseEntity.ok(seedSellerService.getSeedSellerById(id));
    }

    @PutMapping("/seedseller/update/{id}")
    public ResponseEntity<SeedSeller> updateSeedSeller(@PathVariable String id, @RequestBody SeedSeller sellerDetails) {
        return ResponseEntity.ok(seedSellerService.updateSeedSeller(id, sellerDetails));
    }

    @DeleteMapping("/admin/deleteseedseller/{id}")
    public ResponseEntity<Void> deleteSeedSeller(@PathVariable String id) {
        seedSellerService.deleteSeedSeller(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/seedseller/seed/{seedId}")
    public ResponseEntity<List<SeedSeller>> getSellersWithSeed(@PathVariable String seedId) {
        return ResponseEntity.ok(seedSellerService.getSellersWithSeed(seedId));
    }

    @GetMapping("/seedseller/premium")
    public ResponseEntity<List<SeedSeller>> getPremiumSellers(
            @RequestParam double minRating,
            @RequestParam int minYears) {
        return ResponseEntity.ok(seedSellerService.getPremiumSellers(minRating, minYears));
    }

    @GetMapping("/seedseller/top-rated")
    public ResponseEntity<List<SeedSeller>> getTopRatedSellers(@RequestParam int limit) {
        return ResponseEntity.ok(seedSellerService.getTopRatedSellers(limit));
    }
}