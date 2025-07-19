package com.example.server.repository.PlantdiseaseRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.MLmodel.PlantDiseaseRequest;

public interface PlantDiseaseRequestRepository extends MongoRepository<PlantDiseaseRequest, String> {
    List<PlantDiseaseRequest> findByUserId(String userId);

    List<PlantDiseaseRequest> findByStatus(PlantDiseaseRequest.DiseaseStatus status);

    List<PlantDiseaseRequest> findByPredictedDisease(String diseaseName);
}
