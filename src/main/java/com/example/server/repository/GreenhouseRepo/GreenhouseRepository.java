package com.example.server.repository.GreenhouseRepo;



import com.example.server.model.GreenhouseModels.GreenhouseModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GreenhouseRepository extends MongoRepository<GreenhouseModel, String> {
    // Find by name (case-insensitive partial match)
    List<GreenhouseModel> findByNameContainingIgnoreCase(String name);

    // Find by price range
    List<GreenhouseModel> findByBasePriceBetween(double minPrice, double maxPrice);

    // Find by materials (exact match in list)
    List<GreenhouseModel> findByMaterialsContaining(String material);

    // Find by compatible crops (exact match in list)
    List<GreenhouseModel> findByCompatibleCropsContaining(String crop);
}