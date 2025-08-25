package com.example.server.controller.RoleCntroller;

import com.example.server.model.Roles.Buyer;
import com.example.server.service.RolesServices.BuyerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

    @PostMapping("/auth/registerbuyer")
    public ResponseEntity<Buyer> registerBuyer(@RequestBody Buyer buyer) {
        return ResponseEntity.ok(buyerService.registerBuyer(buyer));
    }

    @GetMapping("/admin/allbuyers")
    public ResponseEntity<List<Buyer>> getAllBuyers() {
        return ResponseEntity.ok(buyerService.getAllBuyers());
    }

    @GetMapping("/getbuyer/{id}")
    public ResponseEntity<Buyer> getBuyerById(@PathVariable String id) {
        return ResponseEntity.ok(buyerService.getBuyerById(id));
    }

    @PutMapping("/buyer/{id}")
    public ResponseEntity<Buyer> updateBuyerProfile(@PathVariable String id, @RequestBody Buyer buyerDetails) {
        return ResponseEntity.ok(buyerService.updateBuyerProfile(id, buyerDetails));
    }

    @PostMapping("/{buyerId}/favorites/{farmerId}")
    public ResponseEntity<Buyer> addFavoriteFarmer(
            @PathVariable String buyerId,
            @PathVariable String farmerId) {
        return ResponseEntity.ok(buyerService.addFavoriteFarmer(buyerId, farmerId));
    }

    @DeleteMapping("/{buyerId}/favorites/{farmerId}")
    public ResponseEntity<Buyer> removeFavoriteFarmer(
            @PathVariable String buyerId,
            @PathVariable String farmerId) {
        return ResponseEntity.ok(buyerService.removeFavoriteFarmer(buyerId, farmerId));
    }

    @GetMapping("/favorites/{farmerId}")
    public ResponseEntity<List<Buyer>> getBuyersWhoFavoritedFarmer(@PathVariable String farmerId) {
        return ResponseEntity.ok(buyerService.getBuyersWhoFavoritedFarmer(farmerId));
    }

    @PostMapping("/{buyerId}/orders/{orderId}")
    public ResponseEntity<Buyer> addOrderToHistory(
            @PathVariable String buyerId,
            @PathVariable String orderId) {
        return ResponseEntity.ok(buyerService.addOrderToHistory(buyerId, orderId));
    }

    @GetMapping("/active/{minOrders}")
    public ResponseEntity<List<Buyer>> getActiveBuyers(@PathVariable int minOrders) {
        return ResponseEntity.ok(buyerService.getActiveBuyers(minOrders));
    }

    @DeleteMapping("/admin/deletebuyer/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable String id) {
        buyerService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}