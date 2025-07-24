package com.example.server.service.DeliveryService;

import com.example.server.model.Delivery.Delivery;
import com.example.server.repository.DeliveryRepo.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery createDelivery(Delivery delivery) {
        delivery.setPickupTime(LocalDateTime.now());
        delivery.setStatus(Delivery.DeliveryStatus.PENDING);
        return deliveryRepository.save(delivery);
    }

    public Delivery getDeliveryById(String id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found with id: " + id));
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery updateDelivery(String id, Delivery deliveryDetails) {
        Delivery delivery = getDeliveryById(id);
        
        if (deliveryDetails.getOrderId() != null) {
            delivery.setOrderId(deliveryDetails.getOrderId());
        }
        if (deliveryDetails.getAssignedDriverId() != null) {
            delivery.setAssignedDriverId(deliveryDetails.getAssignedDriverId());
        }
        if (deliveryDetails.getStatus() != null) {
            delivery.setStatus(deliveryDetails.getStatus());
        }
        if (deliveryDetails.getTrackingNumber() != null) {
            delivery.setTrackingNumber(deliveryDetails.getTrackingNumber());
        }
        if (deliveryDetails.getRouteHistory() != null) {
            delivery.setRouteHistory(deliveryDetails.getRouteHistory());
        }
        if (deliveryDetails.getVehicleType() != null) {
            delivery.setVehicleType(deliveryDetails.getVehicleType());
        }
        if (deliveryDetails.getDeliveryFee() != 0) {
            delivery.setDeliveryFee(deliveryDetails.getDeliveryFee());
        }
        
        return deliveryRepository.save(delivery);
    }

    public void deleteDelivery(String id) {
        deliveryRepository.deleteById(id);
    }

    public List<Delivery> getDeliveriesByOrderId(String orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    public List<Delivery> getDeliveriesByDriverId(String driverId) {
        return deliveryRepository.findByAssignedDriverId(driverId);
    }

    public List<Delivery> getDeliveriesByStatus(Delivery.DeliveryStatus status) {
        return deliveryRepository.findByStatus(status);
    }

    public Delivery updateDeliveryStatus(String id, Delivery.DeliveryStatus status) {
        Delivery delivery = getDeliveryById(id);
        delivery.setStatus(status);
        
        if (status == Delivery.DeliveryStatus.PICKED_UP) {
            delivery.setPickupTime(LocalDateTime.now());
        } else if (status == Delivery.DeliveryStatus.DELIVERED) {
            delivery.setDeliveryTime(LocalDateTime.now());
        }
        
        return deliveryRepository.save(delivery);
    }

    public Delivery addLocationUpdate(String id, Delivery.LocationUpdate locationUpdate) {
        Delivery delivery = getDeliveryById(id);
        List<Delivery.LocationUpdate> routeHistory = delivery.getRouteHistory();
        routeHistory.add(locationUpdate);
        delivery.setRouteHistory(routeHistory);
        return deliveryRepository.save(delivery);
    }
}