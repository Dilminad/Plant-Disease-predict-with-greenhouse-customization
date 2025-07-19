package com.example.server.model.MLmodel;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "disease_requests")
public class PlantDiseaseRequest {
    @Id
    private String id;
    private String imagePath;
    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
    private String predictedDisease;
    private double confidence;
    private List<String> treatmentSuggestions;
    private DiseaseStatus status;
    private String feedback; // User feedback on accuracy
    private String originalFilename;
    private String userId; // Null if guest
    private String plantType; // Optional if user provides
    
    public enum DiseaseStatus { PENDING, PROCESSED, FAILED }
}