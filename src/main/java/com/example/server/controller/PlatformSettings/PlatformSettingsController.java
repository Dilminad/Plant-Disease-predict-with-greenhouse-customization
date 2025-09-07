package com.example.server.controller.PlatformSettings;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.server.model.Commission.PlatformSettings;
import com.example.server.service.PlatformSettings.PlatformSettingsService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlatformSettingsController {
    
    @Autowired
    private PlatformSettingsService platformSettingsService;
    
    // CREATE endpoint
    @PostMapping("/admin/create/platform-settings")
    public ResponseEntity<?> createPlatformSettings(
            @RequestParam double platformFeePercentage,
            @RequestParam double fixedPlatformFee,
            @RequestParam String feeCalculationMethod,
            @RequestHeader("X-Admin-Id") String adminId) {
        
        try {
            PlatformSettings existingSettings = platformSettingsService.getPlatformSettings();
            
            // Check if settings already exist and were not created by system
            if (existingSettings != null && !"system".equals(existingSettings.getLastModifiedBy())) {
                return ResponseEntity.badRequest().body(
                    Map.of("error", "Platform settings already exist", "timestamp", LocalDateTime.now())
                );
            }
            
            PlatformSettings newSettings = platformSettingsService.updatePlatformSettings(
                platformFeePercentage, fixedPlatformFee, feeCalculationMethod, adminId);
            
            return ResponseEntity.ok(newSettings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage(), "timestamp", LocalDateTime.now())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Failed to create platform settings", "timestamp", LocalDateTime.now())
            );
        }
    }
    
    // Public endpoint for all users
    @GetMapping("/platform-settings")
    public ResponseEntity<PlatformSettings> getPlatformSettings() {
        PlatformSettings settings = platformSettingsService.getPlatformSettings();
        return ResponseEntity.ok(settings);
    }
    
    // Admin-only endpoint
    @GetMapping("/admin/platform-settings")
    public ResponseEntity<PlatformSettings> getPlatformSettingsAdmin(@RequestHeader("X-Admin-Id") String adminId) {
        PlatformSettings settings = platformSettingsService.getPlatformSettings();
        return ResponseEntity.ok(settings);
    }
    
    @PutMapping("/admin/update/platform-settings")
    public ResponseEntity<?> updatePlatformSettings(
            @RequestBody Map<String, Object> settingsData,
            @RequestHeader("X-Admin-Id") String adminId) {
        
        try {
            double platformFeePercentage = Double.parseDouble(settingsData.get("platformFeePercentage").toString());
            double fixedPlatformFee = Double.parseDouble(settingsData.get("fixedPlatformFee").toString());
            String feeCalculationMethod = (String) settingsData.get("feeCalculationMethod");
            
            PlatformSettings updatedSettings = platformSettingsService.updatePlatformSettings(
                platformFeePercentage, fixedPlatformFee, feeCalculationMethod, adminId);
            
            return ResponseEntity.ok(updatedSettings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage(), "timestamp", LocalDateTime.now())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Invalid request data", "timestamp", LocalDateTime.now())
            );
        }
    }
    
    // Alternative update endpoint using PlatformSettings object
    @PutMapping("/admin/update/platform-settings/object")
    public ResponseEntity<?> updatePlatformSettingsObject(
            @RequestBody PlatformSettings newSettings,
            @RequestHeader("X-Admin-Id") String adminId) {
        
        try {
            PlatformSettings updatedSettings = platformSettingsService.updatePlatformSettings(
                newSettings, adminId);
            
            return ResponseEntity.ok(updatedSettings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage(), "timestamp", LocalDateTime.now())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Invalid request data", "timestamp", LocalDateTime.now())
            );
        }
    }
    
    @GetMapping("/calculate-fee")
    public ResponseEntity<Double> calculatePlatformFee(@RequestParam double orderAmount) {
        double fee = platformSettingsService.calculatePlatformFee(orderAmount);
        return ResponseEntity.ok(fee);
    }
}