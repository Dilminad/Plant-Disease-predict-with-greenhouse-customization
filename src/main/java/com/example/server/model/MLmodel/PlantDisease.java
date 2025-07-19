package com.example.server.model.MLmodel;

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
@Document(collection = "disease_reference")
public class PlantDisease {
    @Id
    private String id;
    private String name;
    private String scientificName;
    private List<String> affectedPlants;
    private String description;
    private List<String> symptoms;
    private List<String> preventionMethods;
    private List<String> treatmentOptions;
    private List<String> referenceImages;
    private String severity; // LOW, MEDIUM, HIGH
}