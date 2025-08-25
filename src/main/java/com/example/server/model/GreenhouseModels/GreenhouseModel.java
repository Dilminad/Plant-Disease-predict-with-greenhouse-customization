package com.example.server.model.GreenhouseModels;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    private String description;

    @Positive
    private BigDecimal basePrice;

    private String dimensions;
    private List<String> includedSensors;
    private List<String> includedActuators;
    private int warrantyMonths;
    private String imageUrl;

    @NotNull
    @Size(min = 1)
    private List<String> compatibleCrops; // For filtering by crops

    @NotNull
    @Size(min = 1)
    private List<String> materials; // Added for filtering by materials

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
}