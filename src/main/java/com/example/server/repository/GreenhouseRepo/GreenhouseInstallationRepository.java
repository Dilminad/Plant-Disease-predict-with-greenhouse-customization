package com.example.server.repository.GreenhouseRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.GreenhouseModels.GreenhouseInstallation;

public interface GreenhouseInstallationRepository extends MongoRepository<GreenhouseInstallation, String> {
    List<GreenhouseInstallation> findByFarmerId(String farmerId);

    List<GreenhouseInstallation> findByStatus(String status);

    List<GreenhouseInstallation> findByInstallationTeam(String teamId);
}
