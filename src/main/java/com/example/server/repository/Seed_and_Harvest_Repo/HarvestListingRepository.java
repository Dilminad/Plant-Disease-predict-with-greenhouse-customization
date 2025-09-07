package com.example.server.repository.Seed_and_Harvest_Repo;

import com.example.server.model.Products.Harvest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HarvestListingRepository extends MongoRepository<Harvest, String> {

    @Query("{ 'expiryDate': { $gt: ?0 }, 'quantityAvailable': { $gt: 0 } }")
    List<Harvest> findAllAvailable(LocalDate currentDate);

    List<Harvest> findByFarmerId(String farmerId);

    List<Harvest> findByProductNameContainingIgnoreCase(String productName);

    List<Harvest> findByOrganicStatus(Harvest.OrganicStatus organicStatus);

    List<Harvest> findByQuantityAvailableGreaterThanEqual(double quantity);
    
    List<Harvest> findByPricePerUnitBetween(double minPrice, double maxPrice);

    List<Harvest> findByHarvestDateAfter(LocalDate date);

    List<Harvest> findByExpiryDateAfter(LocalDate currentDate);

    List<Harvest> findByOrganicStatusAndPricePerUnitBetween(Harvest.OrganicStatus organicStatus, double minPrice, double maxPrice);

    List<Harvest> findByFarmerIdAndExpiryDateBetween(String farmerId, LocalDate startDate, LocalDate endDate);

    // ADD THIS METHOD FOR THE SCHEDULED TASK
    long deleteByExpiryDateBefore(LocalDate currentDate);
}