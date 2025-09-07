package com.example.server.service.RolesServices;

import com.example.server.model.Roles.Buyer;
import com.example.server.repository.RolesRepo.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BuyerService(BuyerRepository buyerRepository, PasswordEncoder passwordEncoder) {
        this.buyerRepository = buyerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE
    public Buyer registerBuyer(Buyer buyer) {
        buyer.setPassword(passwordEncoder.encode(buyer.getPassword()));
        return buyerRepository.save(buyer);
    }

    // READ
    public List<Buyer> getAllBuyers() {
        return buyerRepository.findAll();
    }

    // Add this missing method
    public Buyer getBuyerById(String id) {
        return buyerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buyer not found with id: " + id));
    }

    // UPDATE
    public Buyer updateBuyerProfile(String id, Buyer buyerDetails) {
    Buyer buyer = getBuyerById(id);
    
    // Update all profile fields
    buyer.setFirstname(buyerDetails.getFirstname());
    buyer.setLastname(buyerDetails.getLastname());
    buyer.setEmail(buyerDetails.getEmail());
    buyer.setPhone(buyerDetails.getPhone());
    buyer.setStreet(buyerDetails.getStreet());
    buyer.setCity(buyerDetails.getCity());
    buyer.setState(buyerDetails.getState());
    buyer.setZipCode(buyerDetails.getZipCode());
    buyer.setCountry(buyerDetails.getCountry());
    
    // Update password if provided
    if (buyerDetails.getPassword() != null && !buyerDetails.getPassword().isEmpty()) {
        buyer.setPassword(passwordEncoder.encode(buyerDetails.getPassword()));
    }
    
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

    public List<Buyer> getActiveBuyers(int minimumOrders) {
        return buyerRepository.findAll().stream()
                .filter(b -> b.getOrderHistory() != null && b.getOrderHistory().size() >= minimumOrders)
                .toList();
    }
    public Optional<Buyer> findByUsername(String username) {
    return buyerRepository.findByUsername(username);
}
}