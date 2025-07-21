package com.example.server.Security.Jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.server.model.Roles.Admin;
import com.example.server.model.Roles.Buyer;
import com.example.server.model.Roles.Farmer;
import com.example.server.model.Roles.SeedSeller;
import com.example.server.repository.RolesRepo.AdminRepository;
import com.example.server.repository.RolesRepo.BuyerRepository;
import com.example.server.repository.RolesRepo.FarmerRepository;
import com.example.server.repository.RolesRepo.SeedSellerRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SeedSellerRepository seedSellerRepository;

    @Value("${app.secret:default-secret-key}")
    private String secret;

    @Value("${app.jwt.expiration:86400000}") // Default to 24 hours
    private long expirationTime;

    private Key key() {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        } catch (Exception e) {
            logger.error("Failed to decode secret key: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid secret key", e);
        }
    }

    public String generateJwtToken(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("Authentication or principal cannot be null");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = getRoleFromUserDetails(userDetails);
        String userId = getUserIdFromDatabase(userDetails, role);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String getRoleFromUserDetails(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new IllegalStateException("User has no roles assigned"));
    }

    private String getUserIdFromDatabase(UserDetails userDetails, String role) {
        String username = userDetails.getUsername();
        switch (role) {
            case "ROLE_FARMER":
                Farmer farmer = farmerRepository.findByUsername(username);
                if (farmer == null) throw new RuntimeException("Farmer not found");
                return farmer.getId();
            
            case "ROLE_ADMIN":
                Admin admin = adminRepository.findByUsername(username);
                if (admin == null) throw new RuntimeException("Admin not found");
                return admin.getId();
                
            case "ROLE_BUYER":
                Buyer buyer = buyerRepository.findByUsername(username);
                if (buyer == null) throw new RuntimeException("Buyer not found");
                return buyer.getId();
                
            case "ROLE_SEED_SELLER":
                SeedSeller seedSeller = seedSellerRepository.findByUsername(username);
                if (seedSeller == null) throw new RuntimeException("Seed seller not found");
                return seedSeller.getId();
                
            default:
                throw new RuntimeException("Invalid role: " + role);
        }
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage(), e);
            return false;
        }
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().get("userId", String.class);
    }
}