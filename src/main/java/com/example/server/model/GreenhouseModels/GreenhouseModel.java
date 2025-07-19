package com.example.server.model.GreenhouseModels;

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
@Document(collection = "greenhouse_models")
public class GreenhouseModel {
    @Id
    private String id;
    private String name;
    private String description;
    private double basePrice;
    private String dimensions;
    private List<String> includedSensors;
    private List<String> includedActuators;
    private int warrantyMonths;
    private String imageUrl;
    private List<String> compatibleCrops;
}