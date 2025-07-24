package com.example.server.controller.DeliveryController;

import com.example.server.model.Delivery.Delivery;
import com.example.server.service.DeliveryService.DeliveryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        Delivery createdDelivery = deliveryService.createDelivery(delivery);
        return ResponseEntity.ok(createdDelivery);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable String id) {
        Delivery delivery = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(delivery);
    }

    @GetMapping
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable String id, @RequestBody Delivery deliveryDetails) {
        Delivery updatedDelivery = deliveryService.updateDelivery(id, deliveryDetails);
        return ResponseEntity.ok(updatedDelivery);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable String id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Delivery>> getDeliveriesByOrderId(@PathVariable String orderId) {
        List<Delivery> deliveries = deliveryService.getDeliveriesByOrderId(orderId);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Delivery>> getDeliveriesByDriverId(@PathVariable String driverId) {
        List<Delivery> deliveries = deliveryService.getDeliveriesByDriverId(driverId);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Delivery>> getDeliveriesByStatus(@PathVariable Delivery.DeliveryStatus status) {
        List<Delivery> deliveries = deliveryService.getDeliveriesByStatus(status);
        return ResponseEntity.ok(deliveries);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Delivery> updateDeliveryStatus(
            @PathVariable String id, 
            @RequestParam Delivery.DeliveryStatus status) {
        Delivery delivery = deliveryService.updateDeliveryStatus(id, status);
        return ResponseEntity.ok(delivery);
    }

    @PostMapping("/{id}/location")
    public ResponseEntity<Delivery> addLocationUpdate(
            @PathVariable String id, 
            @RequestBody Delivery.LocationUpdate locationUpdate) {
        Delivery delivery = deliveryService.addLocationUpdate(id, locationUpdate);
        return ResponseEntity.ok(delivery);
    }
}