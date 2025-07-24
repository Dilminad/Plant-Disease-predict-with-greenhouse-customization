package com.example.server.controller.GreenhouseControllers;

import com.example.server.model.GreenhouseModels.GreenhouseInstallation;
import com.example.server.model.GreenhouseModels.GreenhouseModel;
import com.example.server.model.GreenhouseModels.GreenhouseMonitoringData;
import com.example.server.service.GreenhouseService.GreenhouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/greenhouses")
public class GreenhouseController {

    private final GreenhouseService greenhouseService;

    @Autowired
    public GreenhouseController(GreenhouseService greenhouseService) {
        this.greenhouseService = greenhouseService;
    }

    // Greenhouse Model Endpoints
    @GetMapping("/models")
    public ResponseEntity<List<GreenhouseModel>> getAllGreenhouseModels() {
        List<GreenhouseModel> models = greenhouseService.getAllGreenhouseModels();
        return ResponseEntity.ok(models);
    }

    @GetMapping("/models/{id}")
    public ResponseEntity<GreenhouseModel> getGreenhouseModelById(@PathVariable String id) {
        GreenhouseModel model = greenhouseService.getGreenhouseModelById(id);
        return model != null ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
    }

    @GetMapping("/models/search")
    public ResponseEntity<List<GreenhouseModel>> searchGreenhouseModels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String crop) {
        
        if (name != null && crop != null) {
            // Implement combined search logic if needed
            return ResponseEntity.badRequest().build();
        } else if (name != null) {
            return ResponseEntity.ok(greenhouseService.getGreenhouseModelsByName(name));
        } else if (crop != null) {
            return ResponseEntity.ok(greenhouseService.getGreenhouseModelsByCrop(crop));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // Greenhouse Installation Endpoints
    @PostMapping("/installations")
    public ResponseEntity<GreenhouseInstallation> createInstallation(
            @RequestBody GreenhouseInstallation installation) {
        GreenhouseInstallation created = greenhouseService.createInstallation(installation);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/installations/farmer/{farmerId}")
    public ResponseEntity<List<GreenhouseInstallation>> getFarmerInstallations(
            @PathVariable String farmerId) {
        List<GreenhouseInstallation> installations = greenhouseService.getFarmerInstallations(farmerId);
        return ResponseEntity.ok(installations);
    }

    // Monitoring Data Endpoints
    @PostMapping("/monitoring")
    public ResponseEntity<GreenhouseMonitoringData> addMonitoringData(
            @RequestBody GreenhouseMonitoringData data) {
        GreenhouseMonitoringData savedData = greenhouseService.addMonitoringData(data);
        return ResponseEntity.ok(savedData);
    }

    @GetMapping("/monitoring/{installationId}")
    public ResponseEntity<List<GreenhouseMonitoringData>> getInstallationData(
            @PathVariable String installationId,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end) {
        
        if (start == null || end == null) {
            // Return recent data if no time range is specified
            end = LocalDateTime.now();
            start = end.minusDays(1); // Default to last 24 hours
        }
        
        List<GreenhouseMonitoringData> data = 
            greenhouseService.getInstallationData(installationId, start, end);
        return ResponseEntity.ok(data);
    }
}