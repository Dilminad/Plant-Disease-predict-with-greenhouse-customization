package com.example.server.repository.Seed_and_Harvest_Repo;

import com.example.server.model.Products.Harvest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HarvestListingRepository extends MongoRepository<Harvest, String> {

    // Find all harvests by farmerId
    List<Harvest> findByFarmerId(String farmerId);

    // Find harvests by product name (case-insensitive)
    List<Harvest> findByProductNameContainingIgnoreCase(String productName);

    // Find harvests by organic status
    List<Harvest> findByOrganicStatus(Harvest.OrganicStatus organicStatus);

    // Find harvests with quantity available greater than specified value
    List<Harvest> findByQuantityAvailableGreaterThan(double quantity);

    // Find harvests within a price range
    List<Harvest> findByPricePerUnitBetween(double minPrice, double maxPrice);

    // Find harvests harvested after a specific date
    List<Harvest> findByHarvestDateAfter(LocalDate date);

    // Find harvests that haven't expired yet (expiryDate is after current date)
    List<Harvest> findByExpiryDateAfter(LocalDate currentDate);

    // Find harvests by multiple criteria (example: organic and within price range)
    List<Harvest> findByOrganicStatusAndPricePerUnitBetween(
            Harvest.OrganicStatus organicStatus, double minPrice, double maxPrice);
   
    // This will find and delete all documents where the expiryDate is before the given date.
    long deleteByExpiryDateBefore(LocalDate date);
    // Used to alert the farmer about products expiring soon.
    List<Harvest> findByFarmerIdAndExpiryDateBetween(String farmerId, LocalDate start, LocalDate end);
}

