package com.example.server.repository.RolesRepo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.server.model.Roles.Admin;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    List<Admin> findByDepartment(String department);

    List<Admin> findByPermissionsContaining(String permission);
}