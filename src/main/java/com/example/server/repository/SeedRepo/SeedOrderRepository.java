package com.example.server.repository.SeedRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.Marketplace.SeedOrder;

public interface SeedOrderRepository extends MongoRepository <SeedOrder, String>{
     List<SeedOrder> findByFarmerId(String farmerId);
    List<SeedOrder> findBySeedId(String seedId);
    List<SeedOrder> findByStatus(String status);
}


