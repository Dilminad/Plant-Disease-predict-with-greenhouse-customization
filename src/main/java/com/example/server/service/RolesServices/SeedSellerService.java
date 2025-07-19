package com.example.server.service.RolesServices;

import com.example.server.model.Roles.SeedSeller;
import com.example.server.repository.RolesRepo.SeedSellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeedSellerService {

    @Autowired
    private SeedSellerRepository seedSellerRepository;

    // Get sellers with a specific seed (assumes SeedSeller has a seeds field)
    public List<SeedSeller> getSellersWithSeed(String seedId) {
        return seedSellerRepository.findBySeedsContaining(seedId);
    }

    // Get premium sellers based on minimum rating and years in business
    public List<SeedSeller> getPremiumSellers(double minRating, int minYears) {
        return seedSellerRepository.findBySellerRatingGreaterThanEqualAndYearsInBusinessGreaterThanEqual(minRating, minYears);
    }

    // Other methods referenced in the controller (for completeness)
    public SeedSeller registerSeedSeller(SeedSeller seedSeller) {
        return seedSellerRepository.save(seedSeller);
    }

    public List<SeedSeller> getAllSeedSellers() {
        return seedSellerRepository.findAll();
    }

    public SeedSeller getSeedSellerById(String id) {
        return seedSellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seed Seller not found with id: " + id));
    }

    public SeedSeller updateSeedSeller(String id, SeedSeller sellerDetails) {
        SeedSeller seedSeller = getSeedSellerById(id);
        seedSeller.setBusinessLicense(sellerDetails.getBusinessLicense());
        seedSeller.setSellerRating(sellerDetails.getSellerRating());
        seedSeller.setYearsInBusiness(sellerDetails.getYearsInBusiness());
        return seedSellerRepository.save(seedSeller);
    }

    public SeedSeller updateSellerRating(String id, double rating) {
        SeedSeller seedSeller = getSeedSellerById(id);
        seedSeller.setSellerRating(rating);
        return seedSellerRepository.save(seedSeller);
    }

    public List<SeedSeller> getTopRatedSellers(int limit) {
        return seedSellerRepository.findByOrderBySellerRatingDesc()
                .stream()
                .limit(limit)
                .toList();
    }

    public void deleteSeedSeller(String id) {
        seedSellerRepository.deleteById(id);
    }
}