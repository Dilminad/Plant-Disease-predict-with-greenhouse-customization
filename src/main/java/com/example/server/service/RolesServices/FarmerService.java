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

    // UPDATE
    public Farmer updateFarmer(String id, Farmer farmerDetails) {
        Farmer farmer = getFarmerById(id);
        
       // Update inherited fields from User
    farmer.setUsername(farmerDetails.getUsername());
    farmer.setFirstname(farmerDetails.getFirstname());
    farmer.setLastname(farmerDetails.getLastname());
    farmer.setEmail(farmerDetails.getEmail());
    farmer.setPhone(farmerDetails.getPhone());
    farmer.setStreet(farmerDetails.getStreet());
    farmer.setCity(farmerDetails.getCity());
    farmer.setState(farmerDetails.getState());
    farmer.setZipCode(farmerDetails.getZipCode());
    farmer.setCountry(farmerDetails.getCountry());
    farmer.setProfileImageUrl(farmerDetails.getProfileImageUrl());
    
    // Update farmer-specific fields
    farmer.setFarmLocation(farmerDetails.getFarmLocation());
    farmer.setFarmSize(farmerDetails.getFarmSize());
    farmer.setGreenhouseIds(farmerDetails.getGreenhouseIds());
    farmer.setHarvestIds(farmerDetails.getHarvestIds());
    
    // Update password if provided
    if (farmerDetails.getPassword() != null && !farmerDetails.getPassword().isEmpty()) {
        farmer.setPassword(passwordEncoder.encode(farmerDetails.getPassword()));
    }
    
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