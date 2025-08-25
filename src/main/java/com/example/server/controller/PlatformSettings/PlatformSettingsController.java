package com.example.server.controller.PlatformSettings;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.server.model.Commission.PlatformSettings;
import com.example.server.service.PlatformSettings.PlatformSettingsService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class PlatformSettingsController {
    
    @Autowired
    private PlatformSettingsService platformSettingsService;
    
    // CREATE endpoint (optional - usually settings are auto-created)
     @PostMapping("/admin/create/platform-settings")
    public ResponseEntity<PlatformSettings> createPlatformSettings(
            @RequestParam double platformFeePercentage,
            @RequestParam double fixedPlatformFee,
            @RequestParam double minimumPlatformFee,
            @RequestParam double maximumPlatformFee,
            @RequestParam String feeCalculationMethod,
            @RequestHeader("X-Admin-Id") String adminId) {
        
        try {
            // First check if settings already exist
            PlatformSettings existingSettings = platformSettingsService.getPlatformSettings();
            
            if (existingSettings != null && !"system".equals(existingSettings.getLastModifiedBy())) {
                return ResponseEntity.badRequest().body(null); // Settings already exist
            }
            
            PlatformSettings newSettings = platformSettingsService.updatePlatformSettings(
                platformFeePercentage, fixedPlatformFee, minimumPlatformFee, 
                maximumPlatformFee, feeCalculationMethod, adminId);
            
            return ResponseEntity.ok(newSettings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @GetMapping("/admin/platform-settings")
    public ResponseEntity<PlatformSettings> getPlatformSettings() {
        PlatformSettings settings = platformSettingsService.getPlatformSettings();
        return ResponseEntity.ok(settings);
    }
    
    @PutMapping("/admin/update/platform-settings")
    public ResponseEntity<?> updatePlatformSettings(
            @RequestParam double platformFeePercentage,
            @RequestParam double fixedPlatformFee,
            @RequestParam double minimumPlatformFee,
            @RequestParam double maximumPlatformFee,
            @RequestParam String feeCalculationMethod,
            @RequestHeader("X-Admin-Id") String adminId) {
        
        try {
            PlatformSettings updatedSettings = platformSettingsService.updatePlatformSettings(
                platformFeePercentage, fixedPlatformFee, minimumPlatformFee, 
                maximumPlatformFee, feeCalculationMethod, adminId);
            
            return ResponseEntity.ok(updatedSettings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage(), "timestamp", LocalDateTime.now())
            );
        }
    }
    
    @GetMapping("/calculate-fee")
    public ResponseEntity<Double> calculatePlatformFee(@RequestParam double orderAmount) {
        double fee = platformSettingsService.calculatePlatformFee(orderAmount);
        return ResponseEntity.ok(fee);
    }
}