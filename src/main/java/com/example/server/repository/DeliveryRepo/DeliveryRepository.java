package com.example.server.repository.DeliveryRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.server.model.Delivery.Delivery;



public interface DeliveryRepository extends MongoRepository<Delivery, String> {
    List<Delivery> findByOrderId(String orderId);

    List<Delivery> findByAssignedDriverId(String driverId);

    List<Delivery> findByStatus(Delivery.DeliveryStatus status);
}