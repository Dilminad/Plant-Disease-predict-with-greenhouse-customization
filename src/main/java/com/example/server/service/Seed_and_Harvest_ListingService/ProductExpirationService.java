package com.example.server.service.Seed_and_Harvest_ListingService;

import com.example.server.repository.Seed_and_Harvest_Repo.HarvestListingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProductExpirationService {

    private static final Logger logger = LoggerFactory.getLogger(ProductExpirationService.class);

    @Autowired
    private HarvestListingRepository harvestListingRepository;

    /**
     * This task runs automatically every day at 12:20 AM server time.
     * It finds all products where the expiryDate is before the current day and deletes them.
     */
    // The cron expression has been updated to run at 12:20 AM
    @Scheduled(cron = "0 0 2 * * ?") 
    public void removeExpiredProducts() {
        logger.info("Running scheduled task to remove expired products...");
        try {
            long deletedCount = harvestListingRepository.deleteByExpiryDateBefore(LocalDate.now());
            if (deletedCount > 0) {
                logger.info("Successfully removed {} expired products.", deletedCount);
            } else {
                logger.info("No expired products found to remove.");
            }
        } catch (Exception e) {
            logger.error("Error during expired product removal task", e);
        }
    }
}