package com.example.server.controller.SensorController;

import com.example.server.dto.Feed;
import com.example.server.service.ThingServices.ThingSpeakService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/sensor-data")
// This allows your React app on localhost:3000 to access the API
@CrossOrigin(origins = "http://localhost:3000") 
public class SensorDataController {

    private final ThingSpeakService thingSpeakService;

    public SensorDataController(ThingSpeakService thingSpeakService) {
        this.thingSpeakService = thingSpeakService;
    }

    @GetMapping("/latest")
    public ResponseEntity<Feed> getLatestData() {
        Feed latestData = thingSpeakService.getLatestSensorData();
        if (latestData != null) {
            return ResponseEntity.ok(latestData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}