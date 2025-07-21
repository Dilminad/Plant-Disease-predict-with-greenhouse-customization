package com.example.server.repository.RolesRepo;

import com.example.server.model.Roles.Buyer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BuyerRepository extends MongoRepository<Buyer, String> {
    List<Buyer> findByOrderHistoryContaining(String orderId);
    List<Buyer> findByFavoriteFarmersContaining(String farmerId);
    Page<Buyer> findByFavoriteFarmersContaining(String farmerId, Pageable pageable);
    Buyer findByUsername(String username);
    Page<Buyer> findAll(Pageable pageable);
}