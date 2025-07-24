package com.example.server.repository.GreenhouseRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.GreenhouseModels.GreenhouseModel;

public interface GreenhouseModelRepository extends MongoRepository<GreenhouseModel, String> {
    List<GreenhouseModel> findByNameContainingIgnoreCase(String name);

    List<GreenhouseModel> findByCompatibleCropsContains(String cropName);

    
}