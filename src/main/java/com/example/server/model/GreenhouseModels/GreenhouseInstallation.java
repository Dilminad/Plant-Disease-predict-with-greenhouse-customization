package com.example.server.model.GreenhouseModels;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "greenhouse_installations")
public class GreenhouseInstallation {
    @Id
    private String id;
    private String farmerId;
    private String greenhouseModelId;
    private LocalDate orderDate;
    private LocalDate installationDate;
    private String status; // PLANNED, IN_PROGRESS, COMPLETED, MAINTENANCE
    private String location;
    private List<InstallationDevice> devices;
    private String installationTeam;
    private String installationNotes;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InstallationDevice {
        private String deviceId;
        private DeviceType type;
        private String name;
        private String specification;
        private LocalDate lastCalibration;
    }
    
    public enum DeviceType {
        TEMPERATURE_SENSOR, 
        HUMIDITY_SENSOR, 
        SOIL_MOISTURE_SENSOR,
        WATER_VALVE, 
        FAN_CONTROLLER, 
        LIGHT_CONTROLLER
    }
}