package com.example.server.repository.RolesRepo;

import com.example.server.model.Roles.SeedSeller;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeedSellerRepository extends MongoRepository<SeedSeller, String> {

    // Find sellers with a specific seed ID in their seeds list
    List<SeedSeller> findBySeedsContaining(String seedId);

    // Find sellers with minimum rating and years in business
    List<SeedSeller> findBySellerRatingGreaterThanEqualAndYearsInBusinessGreaterThanEqual(double minRating, int minYears);

    // Find sellers ordered by rating (descending)
    List<SeedSeller> findByOrderBySellerRatingDesc();
}