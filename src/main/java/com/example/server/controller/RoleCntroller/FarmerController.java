package com.example.server.controller.RoleCntroller;

import com.example.server.model.Roles.Farmer;
import com.example.server.service.RolesServices.FarmerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class FarmerController {

    @Autowired
    private FarmerService farmerService;

    @PostMapping("/auth/createfarmer")
    public ResponseEntity<Farmer> registerFarmer(@RequestBody Farmer farmer) {
        return ResponseEntity.ok(farmerService.createFarmer(farmer));
    }

    @GetMapping("/admin/allfarmers")
    public ResponseEntity<List<Farmer>> getAllFarmers() {
        return ResponseEntity.ok(farmerService.getAllFarmers());
    }

    @GetMapping("/getfarmer/{id}")
    public ResponseEntity<Farmer> getFarmerById(@PathVariable String id) {
        return ResponseEntity.ok(farmerService.getFarmerById(id));
    }
@PostMapping("/farmer/update/{id}")
public ResponseEntity<Farmer> updateFarmer(@PathVariable String id, 
                                         @RequestBody Farmer farmerDetails,
                                         Principal principal) {
    // Add authentication check - ensure the user is updating their own profile
    Farmer updatedFarmer = farmerService.updateFarmer(id, farmerDetails);
    return ResponseEntity.ok(updatedFarmer);
}

    @GetMapping("/location/{location}")
    public ResponseEntity<List<Farmer>> getFarmersByLocation(@PathVariable String location) {
        return ResponseEntity.ok(farmerService.getFarmersByLocation(location));
    }

    @GetMapping("/size")
    public ResponseEntity<List<Farmer>> getFarmersBySizeRange(
            @RequestParam double min,
            @RequestParam double max) {
        return ResponseEntity.ok(farmerService.getFarmersBySizeRange(min, max));
    }

    @DeleteMapping("/admin/deletefarmer/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable String id) {
        farmerService.deleteFarmer(id);
        return ResponseEntity.noContent().build();
    }
}