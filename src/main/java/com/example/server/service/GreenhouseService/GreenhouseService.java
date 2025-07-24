package com.example.server.service.GreenhouseService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.model.GreenhouseModels.GreenhouseInstallation;
import com.example.server.model.GreenhouseModels.GreenhouseModel;
import com.example.server.model.GreenhouseModels.GreenhouseMonitoringData;
import com.example.server.repository.GreenhouseRepo.GreenhouseInstallationRepository;
import com.example.server.repository.GreenhouseRepo.GreenhouseModelRepository;
import com.example.server.repository.GreenhouseRepo.GreenhouseMonitoringDataRepository; // Import the repository

@Service
public class GreenhouseService {
    @Autowired
    private GreenhouseModelRepository greenhouseModelRepository;

    @Autowired
    private GreenhouseInstallationRepository greenhouseInstallationRepository;

    @Autowired
    private GreenhouseMonitoringDataRepository monitoringDataRepository; // Fix the type here

    public List<GreenhouseModel> getAllGreenhouseModels() {
        return greenhouseModelRepository.findAll();
    }

    public GreenhouseModel getGreenhouseModelById(String id) {
        return greenhouseModelRepository.findById(id).orElse(null);
    }

    public GreenhouseInstallation createInstallation(GreenhouseInstallation installation) {
        return greenhouseInstallationRepository.save(installation);
    }

    public List<GreenhouseInstallation> getFarmerInstallations(String farmerId) {
        return greenhouseInstallationRepository.findByFarmerId(farmerId);
    }

    public GreenhouseMonitoringData addMonitoringData(GreenhouseMonitoringData data) {
        data.setTimestamp(LocalDateTime.now());
        return monitoringDataRepository.save(data);
    }

    public List<GreenhouseMonitoringData> getInstallationData(String installationId, LocalDateTime start,
            LocalDateTime end) {
        return monitoringDataRepository.findByInstallationIdAndTimestampBetween(installationId, start, end);
    }

    public List<GreenhouseModel> getGreenhouseModelsByName(String name) {
        return greenhouseModelRepository.findByNameContainingIgnoreCase(name);
    }

    public List<GreenhouseModel> getGreenhouseModelsByCrop(String cropName) {
        return greenhouseModelRepository.findByCompatibleCropsContains(cropName);
    }
}