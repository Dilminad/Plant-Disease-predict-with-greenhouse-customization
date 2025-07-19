package com.example.server.repository.GreenhouseRepo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.GreenhouseModels.GreenhouseMonitoringData;

public interface GreenhouseMonitoringDataRepository extends MongoRepository<GreenhouseMonitoringData, String> {
    List<GreenhouseMonitoringData> findByInstallationIdAndTimestampBetween(String installationId, LocalDateTime start, LocalDateTime end);
}