package com.example.server.controller;

import com.example.server.model.Roles.Admin;
import com.example.server.repository.RolesRepo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    // Admin-specific operations
    public List<Admin> findByDepartment(String department) {
        return adminRepository.findByDepartment(department);
    }

    // Useful for permission-based access control
    public List<Admin> findByPermission(String permission) {
        return adminRepository.findByPermissionsContaining(permission);
    }

    // Standard CRUD operations
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(String id, Admin adminDetails) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Admin not found"));
        // Only update relevant admin-specific fields
        admin.setDepartment(adminDetails.getDepartment());
        admin.setPermissions(adminDetails.getPermissions());
        return adminRepository.save(admin);
    }
}