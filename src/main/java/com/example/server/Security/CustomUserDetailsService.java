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
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check Admin
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return createUserDetails(admin.getUsername(), admin.getPassword(), "ADMIN");
        }

        // Check Buyer
        Optional<Buyer> buyerOpt = buyerRepository.findByUsername(username);
        if (buyerOpt.isPresent()) {
            Buyer buyer = buyerOpt.get();
            return createUserDetails(buyer.getUsername(), buyer.getPassword(), "BUYER");
        }

        // Check Farmer
        Optional<Farmer> farmerOpt = farmerRepository.findByUsername(username);
        if (farmerOpt.isPresent()) {
            Farmer farmer = farmerOpt.get();
            return createUserDetails(farmer.getUsername(), farmer.getPassword(), "FARMER");
        }

        // Check SeedSeller
        Optional<SeedSeller> seedSellerOpt = seedSellerRepository.findByUsername(username);
        if (seedSellerOpt.isPresent()) {
            SeedSeller seedSeller = seedSellerOpt.get();
            return createUserDetails(seedSeller.getUsername(), seedSeller.getPassword(), "SEED_SELLER");
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    private UserDetails createUserDetails(String username, String password, String role) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        
        return new User(
                username,
                password,
                true, // enabled
                true, // account not expired
                true, // credentials not expired
                true, // account not locked
                authorities
        );
    }
}