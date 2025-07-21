package com.example.server.service.RolesServices;

import com.example.server.model.Roles.Admin;
import com.example.server.repository.RolesRepo.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    // Changed from findAllAdmins() to getAllAdmins() to match controller
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Changed from findAdminById() to getAdminById() to match controller
    public Admin getAdminById(String id) {
        Optional<Admin> admin = adminRepository.findById(id);
        return admin.orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    public Admin updateAdmin(String id, Admin adminDetails) {
        Admin admin = getAdminById(id);
        
        if (adminDetails.getDepartment() != null) {
            admin.setDepartment(adminDetails.getDepartment());
        }
        if (adminDetails.getPermissions() != null) {
            admin.setPermissions(adminDetails.getPermissions());
        }
        
        return adminRepository.save(admin);
    }

    public void deleteAdmin(String id) {
        Admin admin = getAdminById(id);
        adminRepository.delete(admin);
    }

    // Changed from findAdminsByDepartment() to getAdminsByDepartment() to match controller
    public List<Admin> getAdminsByDepartment(String department) {
        return adminRepository.findByDepartment(department);
    }

    // Changed from findAdminsByPermission() to getAdminsByPermission() to match controller
    public List<Admin> getAdminsByPermission(String permission) {
        return adminRepository.findByPermissionsContaining(permission);
    }
}