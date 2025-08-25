package com.example.server.service.RolesServices;

import com.example.server.model.Roles.Farmer;
import com.example.server.repository.RolesRepo.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // UPDATE - Fixed to prevent username/password overwrite and handle Double type
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

        // Handle greenhouseIds and harvestIds if needed
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
        
        // Note: We're intentionally NOT updating username to prevent it from being set to null
        
        return farmerRepository.save(farmer);
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