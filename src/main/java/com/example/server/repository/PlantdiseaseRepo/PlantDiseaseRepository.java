package com.example.server.repository.PlantdiseaseRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.MLmodel.PlantDisease;

public interface PlantDiseaseRepository extends MongoRepository<PlantDisease, String> {
    List<PlantDisease> findByNameContainingIgnoreCase(String name);

    List<PlantDisease> findByAffectedPlantsContains(String plantName);

    List<PlantDisease> findBySeverity(String severity);
}
