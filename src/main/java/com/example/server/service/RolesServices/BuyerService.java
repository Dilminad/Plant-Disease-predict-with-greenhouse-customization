package com.example.server.service.RolesServices;

import com.example.server.model.Roles.Buyer;
import com.example.server.repository.RolesRepo.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuyerService {

    @Autowired
    private BuyerRepository buyerRepository;

    // CREATE
    public Buyer registerBuyer(Buyer buyer) {
        // Could add validation for buyer-specific rules
        return buyerRepository.save(buyer);
    }

    // READ
    public List<Buyer> getAllBuyers() {
        return buyerRepository.findAll();
    }

    public Buyer getBuyerById(String id) {
        return buyerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Buyer not found"));
    }

    // UPDATE
    public Buyer updateBuyerProfile(String id, Buyer buyerDetails) {
        Buyer buyer = getBuyerById(id);
        
        // Update only allowed profile fields
        buyer.setPhone(buyerDetails.getPhone());
        buyer.setStreet(buyerDetails.getStreet());
        buyer.setCity(buyerDetails.getCity());
        // Note: Don't update security-sensitive fields here
        
        return buyerRepository.save(buyer);
    }

    // FAVORITES MANAGEMENT
    public Buyer addFavoriteFarmer(String buyerId, String farmerId) {
        Buyer buyer = getBuyerById(buyerId);
        if (!buyer.getFavoriteFarmers().contains(farmerId)) {
            buyer.getFavoriteFarmers().add(farmerId);
        }
        return buyerRepository.save(buyer);
    }

    public Buyer removeFavoriteFarmer(String buyerId, String farmerId) {
        Buyer buyer = getBuyerById(buyerId);
        buyer.getFavoriteFarmers().remove(farmerId);
        return buyerRepository.save(buyer);
    }

    // ORDER HISTORY MANAGEMENT
    public Buyer addOrderToHistory(String buyerId, String orderId) {
        Buyer buyer = getBuyerById(buyerId);
        buyer.getOrderHistory().add(orderId);
        return buyerRepository.save(buyer);
    }

    // DELETE
    public void deleteBuyer(String id) {
        buyerRepository.deleteById(id);
    }

    // BUSINESS QUERIES
    public List<Buyer> getBuyersWithOrder(String orderId) {
        return buyerRepository.findByOrderHistoryContaining(orderId);
    }

    public List<Buyer> getBuyersWhoFavoritedFarmer(String farmerId) {
        return buyerRepository.findByFavoriteFarmersContaining(farmerId);
    }

    // Useful for analytics
     public List<Buyer> getActiveBuyers(int minimumOrders) {
        return buyerRepository.findAll().stream()
            .filter(b -> b.getOrderHistory() != null && b.getOrderHistory().size() >= minimumOrders)
            .toList();
    }
}