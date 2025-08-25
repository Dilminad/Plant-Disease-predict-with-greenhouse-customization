package com.example.server.service.PlatformSettings;

import com.example.server.model.Commission.PlatformSettings;
import com.example.server.repository.PlatformRepository.PlatformSettingsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlatformSettingsService {
    
    @Autowired
    private PlatformSettingsRepository platformSettingsRepository;
    
    public PlatformSettings getPlatformSettings() {
        Optional<PlatformSettings> settings = platformSettingsRepository.findById("default_settings");
        
        if (settings.isEmpty()) {
            // Initialize with reasonable defaults that admin can change later
            return initializeDefaultSettings();
        }
        
        return settings.get();
    }
    
    private PlatformSettings initializeDefaultSettings() {
        PlatformSettings defaultSettings = new PlatformSettings();
        defaultSettings.setId("default_settings");
        defaultSettings.setPlatformFeePercentage(5.0);      // 5% default percentage
        defaultSettings.setFixedPlatformFee(2.50);          // $2.50 default fixed fee
        defaultSettings.setMinimumPlatformFee(1.0);         // $1.00 minimum
        defaultSettings.setMaximumPlatformFee(50.0);        // $50.00 maximum
        defaultSettings.setFeeCalculationMethod("PERCENTAGE"); // Default to percentage-based
        defaultSettings.setLastModifiedBy("system");
        defaultSettings.setLastModifiedDate(LocalDateTime.now());
        
        return platformSettingsRepository.save(defaultSettings);
    }
    
    public PlatformSettings updatePlatformSettings(double platformFeePercentage,
                                                  double fixedPlatformFee,
                                                  double minimumPlatformFee,
                                                  double maximumPlatformFee,
                                                  String feeCalculationMethod,
                                                  String adminId) {
        
        // Validate inputs
        if (platformFeePercentage < 0 || fixedPlatformFee < 0 || 
            minimumPlatformFee < 0 || maximumPlatformFee < 0) {
            throw new IllegalArgumentException("Fee values cannot be negative");
        }
        
        if (minimumPlatformFee > maximumPlatformFee) {
            throw new IllegalArgumentException("Minimum fee cannot be greater than maximum fee");
        }
        
        if (!feeCalculationMethod.equals("PERCENTAGE") && !feeCalculationMethod.equals("FIXED")) {
            throw new IllegalArgumentException("Fee calculation method must be 'PERCENTAGE' or 'FIXED'");
        }
        
        PlatformSettings settings = getPlatformSettings();
        settings.setPlatformFeePercentage(platformFeePercentage);
        settings.setFixedPlatformFee(fixedPlatformFee);
        settings.setMinimumPlatformFee(minimumPlatformFee);
        settings.setMaximumPlatformFee(maximumPlatformFee);
        settings.setFeeCalculationMethod(feeCalculationMethod);
        settings.setLastModifiedBy(adminId);
        settings.setLastModifiedDate(LocalDateTime.now());
        
        return platformSettingsRepository.save(settings);
    }
    
    public double calculatePlatformFee(double orderAmount) {
        PlatformSettings settings = getPlatformSettings();
        double calculatedFee;
        
        if ("FIXED".equals(settings.getFeeCalculationMethod())) {
            calculatedFee = settings.getFixedPlatformFee();
        } else {
            // Percentage-based calculation
            calculatedFee = orderAmount * (settings.getPlatformFeePercentage() / 100);
        }
        
        // Apply minimum and maximum constraints
        return Math.min(Math.max(settings.getMinimumPlatformFee(), calculatedFee), 
                       settings.getMaximumPlatformFee());
    }
}