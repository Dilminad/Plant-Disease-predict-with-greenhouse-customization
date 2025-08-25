package com.example.server.Security;

import com.example.server.model.Roles.Admin;
import com.example.server.model.Roles.Buyer;
import com.example.server.model.Roles.Farmer;
import com.example.server.model.Roles.SeedSeller;
import com.example.server.repository.RolesRepo.AdminRepository;
import com.example.server.repository.RolesRepo.BuyerRepository;
import com.example.server.repository.RolesRepo.FarmerRepository;
import com.example.server.repository.RolesRepo.SeedSellerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final BuyerRepository buyerRepository;
    private final FarmerRepository farmerRepository;
    private final SeedSellerRepository seedSellerRepository;

    public CustomUserDetailsService(
            AdminRepository adminRepository,
            BuyerRepository buyerRepository,
            FarmerRepository farmerRepository,
            SeedSellerRepository seedSellerRepository) {
        this.adminRepository = adminRepository;
        this.buyerRepository = buyerRepository;
        this.farmerRepository = farmerRepository;
        this.seedSellerRepository = seedSellerRepository;
        if (adminRepository == null || buyerRepository == null || farmerRepository == null || seedSellerRepository == null) {
            System.out.println("One or more repositories are null! Check bean configuration.");
        } else {
            System.out.println("All repositories initialized successfully.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + username);

        try {
            // Check Admin
            Optional<Admin> adminOpt = adminRepository.findByUsername(username);
            if (adminOpt.isPresent()) {
                System.out.println("Admin found: " + username);
                Admin admin = adminOpt.get();
                return createUserDetails(admin.getUsername(), admin.getPassword(), "ADMIN");
            }
            System.out.println("Admin not found for: " + username);

            // Check Buyer
            Optional<Buyer> buyerOpt = buyerRepository.findByUsername(username);
            if (buyerOpt.isPresent()) {
                System.out.println("Buyer found: " + username);
                Buyer buyer = buyerOpt.get();
                return createUserDetails(buyer.getUsername(), buyer.getPassword(), "BUYER");
            }
            System.out.println("Buyer not found for: " + username);

            // Check Farmer
            Optional<Farmer> farmerOpt = farmerRepository.findByUsername(username);
            if (farmerOpt.isPresent()) {
                System.out.println("Farmer found: " + username);
                Farmer farmer = farmerOpt.get();
                return createUserDetails(farmer.getUsername(), farmer.getPassword(), "FARMER");
            }
            System.out.println("Farmer not found for: " + username);

            // Check SeedSeller
            Optional<SeedSeller> seedSellerOpt = seedSellerRepository.findByUsername(username);
            if (seedSellerOpt.isPresent()) {
                System.out.println("SeedSeller found: " + username);
                SeedSeller seedSeller = seedSellerOpt.get();
                return createUserDetails(seedSeller.getUsername(), seedSeller.getPassword(), "SEED_SELLER");
            }
            System.out.println("SeedSeller not found for: " + username);

            System.out.println("User not found, throwing exception for: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        } catch (Exception e) {
            System.out.println("Exception occurred in loadUserByUsername: " + e.getMessage());
            e.printStackTrace();
            throw new UsernameNotFoundException("Error loading user: " + e.getMessage());
        }
    }

    private UserDetails createUserDetails(String username, String password, String role) {
        System.out.println("Creating UserDetails for: " + username + " with role: " + role);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return new User(
                username,
                password, // Spring Security handles the hashed password comparison
                true, // enabled
                true, // account not expired
                true, // credentials not expired
                true, // account not locked
                authorities
        );
    }
}