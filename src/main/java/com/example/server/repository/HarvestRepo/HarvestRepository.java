package com.example.server.repository.HarvestRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.Products.Harvest;

public interface HarvestRepository extends MongoRepository<Harvest, String> {
    List<Harvest> findByFarmerId(String farmerId);

    List<Harvest> findByProductNameContainingIgnoreCase(String productName);

    List<Harvest> findByOrganicStatus(Harvest.OrganicStatus status);
}