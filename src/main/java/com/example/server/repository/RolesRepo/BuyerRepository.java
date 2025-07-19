package com.example.server.repository.RolesRepo;

import com.example.server.model.Roles.Buyer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BuyerRepository extends MongoRepository<Buyer, String> {
    
    // Basic queries
    List<Buyer> findByOrderHistoryContaining(String orderId);
    
    // Favorite farmers queries
    List<Buyer> findByFavoriteFarmersContaining(String farmerId);
    Page<Buyer> findByFavoriteFarmersContaining(String farmerId, Pageable pageable);
   
    
    // Paginated queries
    Page<Buyer> findAll(Pageable pageable);
}