package com.example.server.controller.SensorController;

import com.example.server.dto.Feed;
import com.example.server.model.Roles.Farmer;
import com.example.server.service.ThingServices.ThingSpeakService;
import com.example.server.service.RolesServices.FarmerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth/sensor-data")
@CrossOrigin(origins = "http://localhost:3000")
public class SensorDataController {

    private final ThingSpeakService thingSpeakService;
    private final FarmerService farmerService;

    public SensorDataController(ThingSpeakService thingSpeakService, FarmerService farmerService) {
        this.thingSpeakService = thingSpeakService;
        this.farmerService = farmerService;
    }

    @GetMapping("/latest")
    public ResponseEntity<Feed> getLatestData() {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        String username = authentication.getName();

        // Check if user is a farmer with greenhouses
        Optional<Farmer> optionalFarmer = farmerService.findByUsername(username);
        if (!optionalFarmer.isPresent()) {
            return ResponseEntity.status(403).build(); // Forbidden: Not a farmer
        }
        Farmer farmer = optionalFarmer.get();
        if (farmer.getGreenhouseIds() == null || farmer.getGreenhouseIds().isEmpty()) {
            return ResponseEntity.status(403).build(); // Forbidden: No greenhouses
        }

        // Proceed if authorized
        Feed latestData = thingSpeakService.getLatestSensorData();
        if (latestData != null) {
            return ResponseEntity.ok(latestData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}