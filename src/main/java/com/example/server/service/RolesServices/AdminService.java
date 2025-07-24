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
        
        if (adminDetails.getDepartment() != null) {
            admin.setDepartment(adminDetails.getDepartment());
        }
        if (adminDetails.getPermissions() != null) {
            admin.setPermissions(adminDetails.getPermissions());
        }
        if (adminDetails.getPassword() != null && !adminDetails.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(adminDetails.getPassword()));
        }
        
        return adminRepository.save(admin);
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