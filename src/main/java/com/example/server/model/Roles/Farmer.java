package com.example.server.model.Roles;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "farmers")
public class Farmer extends User {
    private String farmLocation;
    private double farmSize;
    private List<String> greenhouseIds;
    private List<String> harvestIds;

    public Farmer(String username, String password, String email, String phone,
                 String street, String city, String state, String zipCode, String country,
                 String farmLocation, double farmSize, List<String> greenhouseIds, List<String> harvestIds) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.farmLocation = farmLocation;
        this.farmSize = farmSize;
        this.greenhouseIds = greenhouseIds;
        this.harvestIds = harvestIds;
    }

    @Override
    public String getRole() {
        return "FARMER";
    }
}