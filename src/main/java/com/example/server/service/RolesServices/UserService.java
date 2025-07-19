package com.example.server.service.RolesServices;

import com.example.server.model.Roles.User;
import com.example.server.repository.RolesRepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    // CREATE
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // READ
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // UPDATE
    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update all updatable fields
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        // Add other fields as needed
        
        return userRepository.save(user);
    }

    // DELETE
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    // Specialized queries
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Used during registration
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}