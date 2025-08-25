package com.example.server.service.RolesServices;

import com.example.server.model.Roles.SeedSeller;
import com.example.server.repository.RolesRepo.SeedSellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeedSellerService {

    private final SeedSellerRepository seedSellerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SeedSellerService(SeedSellerRepository seedSellerRepository, PasswordEncoder passwordEncoder) {
        this.seedSellerRepository = seedSellerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<SeedSeller> getSellersWithSeed(String seedId) {
        return seedSellerRepository.findBySeedsContaining(seedId);
    }

    public List<SeedSeller> getPremiumSellers(double minRating, int minYears) {
        return seedSellerRepository.findBySellerRatingGreaterThanEqualAndYearsInBusinessGreaterThanEqual(minRating, minYears);
    }

    public SeedSeller registerSeedSeller(SeedSeller seedSeller) {
        seedSeller.setPassword(passwordEncoder.encode(seedSeller.getPassword()));
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
        seedSeller.setUsername(sellerDetails.getUsername());
        seedSeller.setFirstname(sellerDetails.getFirstname());
        seedSeller.setLastname(sellerDetails.getLastname());
        seedSeller.setEmail(sellerDetails.getEmail());
        seedSeller.setPhone(sellerDetails.getPhone());
        seedSeller.setStreet(sellerDetails.getStreet());
        seedSeller.setCity(sellerDetails.getCity());
        seedSeller.setState(sellerDetails.getState());
        seedSeller.setZipCode(sellerDetails.getZipCode());
        seedSeller.setCountry(sellerDetails.getCountry());
        seedSeller.setProfileImageUrl(sellerDetails.getProfileImageUrl());
        seedSeller.setBusinessLicense(sellerDetails.getBusinessLicense());
        seedSeller.setSellerRating(sellerDetails.getSellerRating());
        seedSeller.setYearsInBusiness(sellerDetails.getYearsInBusiness());
        if (sellerDetails.getPassword() != null && !sellerDetails.getPassword().isEmpty()) {
            seedSeller.setPassword(passwordEncoder.encode(sellerDetails.getPassword()));
        }
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