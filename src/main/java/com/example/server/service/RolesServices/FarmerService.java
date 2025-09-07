package com.example.server.service.RolesServices;

import com.example.server.model.Roles.Farmer;
import com.example.server.repository.RolesRepo.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerService {
    private final FarmerRepository farmerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FarmerService(FarmerRepository farmerRepository, PasswordEncoder passwordEncoder) {
        this.farmerRepository = farmerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    public Farmer createFarmer(Farmer farmer) {
        farmer.setPassword(passwordEncoder.encode(farmer.getPassword()));
        return farmerRepository.save(farmer);
    }

    // READ
    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    public Farmer getFarmerById(String id) {
        return farmerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Farmer not found with id: " + id));
    }

    // Added method to find farmer by username
    public Optional<Farmer> findByUsername(String username) {
        return farmerRepository.findByUsername(username);
    }

    // UPDATE - FIXED to properly handle greenhouseIds
    public Farmer updateFarmer(String id, Farmer farmerDetails) {
        Farmer farmer = getFarmerById(id);
        
        // Only update fields that are provided (non-null)
        if (farmerDetails.getFirstname() != null) {
            farmer.setFirstname(farmerDetails.getFirstname());
        }
        if (farmerDetails.getLastname() != null) {
            farmer.setLastname(farmerDetails.getLastname());
        }
        if (farmerDetails.getEmail() != null) {
            farmer.setEmail(farmerDetails.getEmail());
        }
        if (farmerDetails.getPhone() != null) {
            farmer.setPhone(farmerDetails.getPhone());
        }
        if (farmerDetails.getStreet() != null) {
            farmer.setStreet(farmerDetails.getStreet());
        }
        if (farmerDetails.getCity() != null) {
            farmer.setCity(farmerDetails.getCity());
        }
        if (farmerDetails.getState() != null) {
            farmer.setState(farmerDetails.getState());
        }
        if (farmerDetails.getZipCode() != null) {
            farmer.setZipCode(farmerDetails.getZipCode());
        }
        if (farmerDetails.getCountry() != null) {
            farmer.setCountry(farmerDetails.getCountry());
        }
        if (farmerDetails.getProfileImageUrl() != null) {
            farmer.setProfileImageUrl(farmerDetails.getProfileImageUrl());
        }
        
        // Update farmer-specific fields
        if (farmerDetails.getFarmLocation() != null) {
            farmer.setFarmLocation(farmerDetails.getFarmLocation());
        }
        if (farmerDetails.getFarmSize() != null) {
            farmer.setFarmSize(farmerDetails.getFarmSize());
        }

        // Handle greenhouseIds - FIXED: Only update if provided, don't overwrite with null
        if (farmerDetails.getGreenhouseIds() != null) {
            farmer.setGreenhouseIds(farmerDetails.getGreenhouseIds());
        }
        if (farmerDetails.getHarvestIds() != null) {
            farmer.setHarvestIds(farmerDetails.getHarvestIds());
        }

        // Only update password if provided and not empty
        if (farmerDetails.getPassword() != null && !farmerDetails.getPassword().isEmpty()) {
            farmer.setPassword(passwordEncoder.encode(farmerDetails.getPassword()));
        }
        
        return farmerRepository.save(farmer);
    }

    // NEW METHOD: Add greenhouse to farmer
    public Farmer addGreenhouseToFarmer(String farmerId, String greenhouseId) {
        Farmer farmer = getFarmerById(farmerId);
        List<String> greenhouseIds = farmer.getGreenhouseIds();
        
        if (greenhouseIds == null) {
            greenhouseIds = new java.util.ArrayList<>();
            farmer.setGreenhouseIds(greenhouseIds);
        }
        
        if (!greenhouseIds.contains(greenhouseId)) {
            greenhouseIds.add(greenhouseId);
            return farmerRepository.save(farmer);
        }
        
        return farmer; // Greenhouse already exists
    }

    // DELETE
    public void deleteFarmer(String id) {
        farmerRepository.deleteById(id);
    }

    // Business-specific queries
    public List<Farmer> getFarmersByLocation(String location) {
        return farmerRepository.findByFarmLocation(location);
    }

    public List<Farmer> getFarmersBySizeRange(double min, double max) {
        return farmerRepository.findByFarmSizeBetween(min, max);
    }
}