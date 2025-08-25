package com.example.server.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feed {
    @JsonProperty("field1")
    private String temperature;

    @JsonProperty("field2")
    private String humidity;
    
    @JsonProperty("field3")
    private String soilMoisture;

    @JsonProperty("created_at")
    private String createdAt;

    // Getters and Setters
    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }
    public String getHumidity() { return humidity; }
    public void setHumidity(String humidity) { this.humidity = humidity; }
    public String getSoilMoisture() { return soilMoisture; }
    public void setSoilMoisture(String soilMoisture) { this.soilMoisture = soilMoisture; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}