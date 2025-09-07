package com.example.server.service.Seed_and_Harvest_ListingService;

import com.example.server.model.Products.Harvest;
import com.example.server.repository.Seed_and_Harvest_Repo.HarvestListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HarvestService {

    private final HarvestListingRepository harvestListingRepository;

    @Autowired
    public HarvestService(HarvestListingRepository harvestListingRepository) {
        this.harvestListingRepository = harvestListingRepository;
    }

    public List<Harvest> getAllAvailableHarvests() {
        return harvestListingRepository.findAllAvailable(LocalDate.now());
    }

    public Optional<Harvest> getHarvestById(String id) {
        return harvestListingRepository.findById(id);
    }

    public List<Harvest> getHarvestsByFarmerId(String farmerId) {
        return harvestListingRepository.findByFarmerId(farmerId);
    }

    public List<Harvest> searchHarvestsByProductName(String productName) {
        return harvestListingRepository.findByProductNameContainingIgnoreCase(productName);
    }

    public List<Harvest> getHarvestsByOrganicStatus(Harvest.OrganicStatus organicStatus) {
        return harvestListingRepository.findByOrganicStatus(organicStatus);
    }

    public List<Harvest> getHarvestsWithMinQuantity(double quantity) {
        return harvestListingRepository.findByQuantityAvailableGreaterThanEqual(quantity);
    }

    public List<Harvest> getHarvestsInPriceRange(double minPrice, double maxPrice) {
        return harvestListingRepository.findByPricePerUnitBetween(minPrice, maxPrice);
    }

    public List<Harvest> getRecentHarvests(LocalDate date) {
        return harvestListingRepository.findByHarvestDateAfter(date);
    }

    public List<Harvest> getNonExpiredHarvests(LocalDate currentDate) {
        return harvestListingRepository.findByExpiryDateAfter(currentDate);
    }

    public List<Harvest> getOrganicHarvestsInPriceRange(Harvest.OrganicStatus organicStatus,
                                                         double minPrice, double maxPrice) {
        return harvestListingRepository.findByOrganicStatusAndPricePerUnitBetween(
                organicStatus, minPrice, maxPrice);
    }

    public Harvest createHarvest(Harvest harvest) {
        return harvestListingRepository.save(harvest);
    }

    public Harvest updateHarvest(String id, Harvest updatedHarvest) {
        return harvestListingRepository.findById(id)
                .map(existingHarvest -> {
                    existingHarvest.setFarmerId(updatedHarvest.getFarmerId());
                    existingHarvest.setProductName(updatedHarvest.getProductName());
                    existingHarvest.setDescription(updatedHarvest.getDescription());
                    existingHarvest.setQuantityAvailable(updatedHarvest.getQuantityAvailable());
                    existingHarvest.setPricePerUnit(updatedHarvest.getPricePerUnit());
                    existingHarvest.setHarvestDate(updatedHarvest.getHarvestDate());
                    existingHarvest.setExpiryDate(updatedHarvest.getExpiryDate());
                    existingHarvest.setOrganicStatus(updatedHarvest.getOrganicStatus());
                    existingHarvest.setCertifications(updatedHarvest.getCertifications());
                    existingHarvest.setImageUrls(updatedHarvest.getImageUrls());
                    return harvestListingRepository.save(existingHarvest);
                })
                .orElseGet(() -> {
                    updatedHarvest.setId(id);
                    return harvestListingRepository.save(updatedHarvest);
                });
    }

    public void deleteHarvest(String id) {
        harvestListingRepository.deleteById(id);
    }

    public List<Harvest> getExpiringSoonProductsForFarmer(String farmerId, LocalDate start, LocalDate end) {
        return harvestListingRepository.findByFarmerIdAndExpiryDateBetween(farmerId, start, end);
    }
}