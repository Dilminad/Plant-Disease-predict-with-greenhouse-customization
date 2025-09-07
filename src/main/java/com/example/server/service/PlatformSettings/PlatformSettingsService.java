package com.example.server.service.PlatformSettings;


import com.example.server.model.Commission.PlatformSettings;
import com.example.server.repository.PlatformRepository.PlatformSettingsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PlatformSettingsService {

    private final PlatformSettingsRepository platformSettingsRepository;

    // Changed from private to public
    public PlatformSettings getPlatformSettings() {
        return platformSettingsRepository.findById("default")
            .orElseGet(() -> {
                // Create default settings if not exists
                PlatformSettings defaultSettings = new PlatformSettings();
                defaultSettings.setId("default");
                defaultSettings.setPlatformFeePercentage(5.0);
                defaultSettings.setFixedPlatformFee(2.50);
                defaultSettings.setFeeCalculationMethod("PERCENTAGE");
                defaultSettings.setLastModifiedBy("system");
                defaultSettings.setLastModifiedDate(LocalDateTime.now());
                return platformSettingsRepository.save(defaultSettings);
            });
    }

    public double getPlatformFeePercentage() {
        PlatformSettings settings = getPlatformSettings();
        return settings.getPlatformFeePercentage();
    }

    public double getFixedPlatformFee() {
        PlatformSettings settings = getPlatformSettings();
        return settings.getFixedPlatformFee();
    }

    public String getFeeCalculationMethod() {
        PlatformSettings settings = getPlatformSettings();
        return settings.getFeeCalculationMethod();
    }

    public double calculatePlatformFee(double orderAmount) {
        PlatformSettings settings = getPlatformSettings();
        
        if ("FIXED".equalsIgnoreCase(settings.getFeeCalculationMethod())) {
            return settings.getFixedPlatformFee();
        } else {
            // Default to percentage-based calculation
            return orderAmount * (settings.getPlatformFeePercentage() / 100);
        }
    }

    public PlatformSettings updatePlatformSettings(double platformFeePercentage, 
                                                  double fixedPlatformFee, 
                                                  String feeCalculationMethod, 
                                                  String modifiedBy) {
        
        // Validate inputs
        if (platformFeePercentage < 0 || platformFeePercentage > 100) {
            throw new IllegalArgumentException("Platform fee percentage must be between 0 and 100");
        }
        
        if (fixedPlatformFee < 0) {
            throw new IllegalArgumentException("Fixed platform fee cannot be negative");
        }
        
        if (!"PERCENTAGE".equalsIgnoreCase(feeCalculationMethod) && 
            !"FIXED".equalsIgnoreCase(feeCalculationMethod)) {
            throw new IllegalArgumentException("Fee calculation method must be either 'PERCENTAGE' or 'FIXED'");
        }
        
        PlatformSettings existingSettings = getPlatformSettings();
        
        existingSettings.setPlatformFeePercentage(platformFeePercentage);
        existingSettings.setFixedPlatformFee(fixedPlatformFee);
        existingSettings.setFeeCalculationMethod(feeCalculationMethod.toUpperCase());
        existingSettings.setLastModifiedBy(modifiedBy);
        existingSettings.setLastModifiedDate(LocalDateTime.now());
        
        return platformSettingsRepository.save(existingSettings);
    }

    public PlatformSettings updatePlatformSettings(PlatformSettings newSettings, String modifiedBy) {
        return updatePlatformSettings(
            newSettings.getPlatformFeePercentage(),
            newSettings.getFixedPlatformFee(),
            newSettings.getFeeCalculationMethod(),
            modifiedBy
        );
    }

    public PlatformSettings getCurrentSettings() {
        return getPlatformSettings();
    }
}