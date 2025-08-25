package com.example.server.service.RolesServices;

import com.example.server.model.Roles.Admin;
import com.example.server.repository.RolesRepo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin createAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(String id) {
        Optional<Admin> admin = adminRepository.findById(id);
        return admin.orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    public Admin updateAdmin(String id, Admin adminDetails) {
        Admin admin = getAdminById(id);
        if (adminDetails.getUsername() != null && !adminDetails.getUsername().isEmpty()) {
            admin.setUsername(adminDetails.getUsername());
        }
        if (adminDetails.getEmail() != null && !adminDetails.getEmail().isEmpty()) {
            admin.setEmail(adminDetails.getEmail());
        }
        if (adminDetails.getPhone() != null) {
            admin.setPhone(adminDetails.getPhone());
        }
        if (adminDetails.getFirstname() != null) {
            admin.setFirstname(adminDetails.getFirstname());
        }
        if (adminDetails.getLastname() != null) {
            admin.setLastname(adminDetails.getLastname());
        }
        if (adminDetails.getProfileImageUrl() != null) {
            admin.setProfileImageUrl(adminDetails.getProfileImageUrl());
        }
        if (adminDetails.getStreet() != null) {
            admin.setStreet(adminDetails.getStreet());
        }
        if (adminDetails.getCity() != null) {
            admin.setCity(adminDetails.getCity());
        }
        if (adminDetails.getState() != null) {
            admin.setState(adminDetails.getState());
        }
        if (adminDetails.getZipCode() != null) {
            admin.setZipCode(adminDetails.getZipCode());
        }
        if (adminDetails.getCountry() != null) {
            admin.setCountry(adminDetails.getCountry());
        }
        if (adminDetails.getDepartment() != null) {
            admin.setDepartment(adminDetails.getDepartment());
        }
        if (adminDetails.getPermissions() != null) {
            admin.setPermissions(adminDetails.getPermissions());
        }
        return adminRepository.save(admin);
    }

    public void updatePassword(String id, String currentPassword, String newPassword) {
        Admin admin = getAdminById(id);
        if (!passwordEncoder.matches(currentPassword, admin.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        if (newPassword == null || newPassword.length() < 8) {
            throw new RuntimeException("New password must be at least 8 characters long");
        }
        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
    }

    public void deleteAdmin(String id) {
        Admin admin = getAdminById(id);
        adminRepository.delete(admin);
    }

    public List<Admin> getAdminsByDepartment(String department) {
        return adminRepository.findByDepartment(department);
    }

    public List<Admin> getAdminsByPermission(String permission) {
        return adminRepository.findByPermissionsContaining(permission);
    }
}