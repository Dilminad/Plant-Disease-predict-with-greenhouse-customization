package com.example.server.repository.RolesRepo;

import com.example.server.model.Roles.Buyer;
import com.example.server.model.Roles.SeedSeller;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeedSellerRepository extends MongoRepository<SeedSeller, String> {

     List<SeedSeller> findBySeedsContaining(String seedId);
    List<SeedSeller> findBySellerRatingGreaterThanEqualAndYearsInBusinessGreaterThanEqual(double minRating, int minYears);
    List<SeedSeller> findByOrderBySellerRatingDesc();
    Optional<SeedSeller> findByUsername(String username);
}