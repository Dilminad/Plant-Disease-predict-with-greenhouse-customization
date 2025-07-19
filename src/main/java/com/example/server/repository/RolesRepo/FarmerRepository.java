package com.example.server.repository.RolesRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.server.model.Roles.Farmer;
import com.example.server.model.Roles.User;

@Repository
public interface FarmerRepository extends MongoRepository <Farmer , String> {
     List<Farmer> findByFarmLocation(String location);
    List<Farmer> findByFarmSizeBetween(double minSize, double maxSize);
    List<Farmer> findByGreenhouseIdsContaining(String greenhouseId);
     
    boolean existsByEmail(String email);
}

