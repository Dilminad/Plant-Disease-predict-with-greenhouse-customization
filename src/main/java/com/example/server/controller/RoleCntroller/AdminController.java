package com.example.server.controller.RoleCntroller;

import com.example.server.model.Roles.Admin;
import com.example.server.service.RolesServices.AdminService;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/auth/createAdmin")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        Admin createdAdmin = adminService.createAdmin(admin);
        return ResponseEntity.ok(createdAdmin);
    }

    @GetMapping("/admin/alladmins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String id) {
        Admin admin = adminService.getAdminById(id);
        return ResponseEntity.ok(admin);
    }

    @PutMapping("/admin/admin-update/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable String id, @RequestBody Admin adminDetails) {
        Admin updatedAdmin = adminService.updateAdmin(id, adminDetails);
        return ResponseEntity.ok(updatedAdmin);
    }

    @PutMapping("/admin/update-password/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable String id, @RequestBody Map<String, String> passwordData) {
        adminService.updatePassword(id, passwordData.get("currentPassword"), passwordData.get("newPassword"));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/deletea/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/department/{department}")
    public ResponseEntity<List<Admin>> getAdminsByDepartment(@PathVariable String department) {
        List<Admin> admins = adminService.getAdminsByDepartment(department);
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/permission/{permission}")
    public ResponseEntity<List<Admin>> getAdminsByPermission(@PathVariable String permission) {
        List<Admin> admins = adminService.getAdminsByPermission(permission);
        return ResponseEntity.ok(admins);
    }
}