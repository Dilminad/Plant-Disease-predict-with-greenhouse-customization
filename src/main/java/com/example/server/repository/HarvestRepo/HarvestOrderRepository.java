package com.example.server.repository.HarvestRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.Marketplace.HarvestOrder;

public interface HarvestOrderRepository extends MongoRepository<HarvestOrder, String> {
    List<HarvestOrder> findByBuyerId(String buyerId);

    List<HarvestOrder> findByHarvestId(String harvestId);

    List<HarvestOrder> findByStatus(String status);
}