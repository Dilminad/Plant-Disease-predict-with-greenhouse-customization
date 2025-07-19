package com.example.server.model.GreenhouseModels;

import java.time.LocalDateTime;
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
@Document(collection = "greenhouse_monitoring")
public class GreenhouseMonitoringData {
    @Id
    private String id;
    private String installationId;
    private String deviceId;
    private double value;
    private LocalDateTime timestamp;
    private String unit;
    private String status = "NORMAL"; // NORMAL, WARNING, CRITICAL
}