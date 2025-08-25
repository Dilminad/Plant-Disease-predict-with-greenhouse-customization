package com.example.server.service.GreenhouseService;

import com.example.server.model.GreenhouseModels.GreenhouseModel;
import com.example.server.repository.GreenhouseRepo.GreenhouseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GreenhouseService {

    @Autowired
    private GreenhouseRepository greenhouseRepository;

    // Create a new greenhouse
    public GreenhouseModel createGreenhouse(GreenhouseModel greenhouse) {
        if (greenhouse.getBasePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Base price cannot be negative");
        }
        greenhouse.setCreatedAt(LocalDateTime.now());
        greenhouse.setUpdatedAt(LocalDateTime.now());
        greenhouse.setActive(true);
        return greenhouseRepository.save(greenhouse);
    }

    // Get all greenhouses
    public List<GreenhouseModel> getAllGreenhouses() {
        return greenhouseRepository.findAll();
    }

    // Get greenhouse by ID
    public Optional<GreenhouseModel> getGreenhouseById(String id) {
        return greenhouseRepository.findById(id);
    }

    // Update a greenhouse
    public GreenhouseModel updateGreenhouse(String id, GreenhouseModel updatedGreenhouse) {
        Optional<GreenhouseModel> existing = greenhouseRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Greenhouse with ID " + id + " not found");
        }
        GreenhouseModel greenhouse = existing.get();
        greenhouse.setName(updatedGreenhouse.getName());
        greenhouse.setDescription(updatedGreenhouse.getDescription());
        greenhouse.setBasePrice(updatedGreenhouse.getBasePrice());
        greenhouse.setDimensions(updatedGreenhouse.getDimensions());
        greenhouse.setIncludedSensors(updatedGreenhouse.getIncludedSensors());
        greenhouse.setIncludedActuators(updatedGreenhouse.getIncludedActuators());
        greenhouse.setWarrantyMonths(updatedGreenhouse.getWarrantyMonths());
        greenhouse.setImageUrl(updatedGreenhouse.getImageUrl());
        greenhouse.setCompatibleCrops(updatedGreenhouse.getCompatibleCrops());
        greenhouse.setMaterials(updatedGreenhouse.getMaterials()); // Added
        greenhouse.setUpdatedAt(LocalDateTime.now());
        return greenhouseRepository.save(greenhouse);
    }

    // Delete a greenhouse
    public void deleteGreenhouse(String id) {
        if (!greenhouseRepository.existsById(id)) {
            throw new IllegalArgumentException("Greenhouse with ID " + id + " not found");
        }
        greenhouseRepository.deleteById(id);
    }

    // Search by name
    public List<GreenhouseModel> searchByName(String name) {
        return greenhouseRepository.findByNameContainingIgnoreCase(name);
    }

    // Filter by price range
    public List<GreenhouseModel> filterByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < minPrice) {
            throw new IllegalArgumentException("Invalid price range");
        }
        return greenhouseRepository.findByBasePriceBetween(minPrice, maxPrice);
    }

    // Filter by material
    public List<GreenhouseModel> filterByMaterial(String material) {
        if (material == null || material.trim().isEmpty()) {
            throw new IllegalArgumentException("Material cannot be empty");
        }
        return greenhouseRepository.findByMaterialsContaining(material);
    }

    // Filter by compatible crop
    public List<GreenhouseModel> filterByCompatibleCrop(String crop) {
        if (crop == null || crop.trim().isEmpty()) {
            throw new IllegalArgumentException("Crop cannot be empty");
        }
        return greenhouseRepository.findByCompatibleCropsContaining(crop);
    }
}