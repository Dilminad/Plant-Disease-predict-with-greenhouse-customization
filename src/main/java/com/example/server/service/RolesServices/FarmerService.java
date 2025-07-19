package com.example.server.service.RolesServices;

import com.example.server.model.Roles.Farmer;
import com.example.server.repository.RolesRepo.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmerService {
    @Autowired
    private FarmerRepository farmerRepository;

    // CREATE
    public Farmer createFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    // READ
    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    public Farmer getFarmerById(String id) {
        return farmerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Farmer not found"));
    }

    // UPDATE
    public Farmer updateFarmer(String id, Farmer farmerDetails) {
        Farmer farmer = getFarmerById(id);
        
        // Update farmer-specific fields
        farmer.setFarmLocation(farmerDetails.getFarmLocation());
        farmer.setFarmSize(farmerDetails.getFarmSize());
        farmer.setGreenhouseIds(farmerDetails.getGreenhouseIds());
        farmer.setHarvestIds(farmerDetails.getHarvestIds());
        
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