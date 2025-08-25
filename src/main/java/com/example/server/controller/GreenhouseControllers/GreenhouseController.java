package com.example.server.controller.GreenhouseControllers;

import com.example.server.model.GreenhouseModels.GreenhouseModel;
import com.example.server.service.GreenhouseService.GreenhouseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController

public class GreenhouseController {

    @Autowired
    private GreenhouseService greenhouseService;

    // Create a new greenhouse
   @PostMapping("/admin/addgreenhouse")
    public ResponseEntity<GreenhouseModel> createGreenhouse(@Valid @RequestBody GreenhouseModel greenhouse) {
        GreenhouseModel created = greenhouseService.createGreenhouse(greenhouse);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Get all greenhouses
    @GetMapping("/allgreenhouses")
    public ResponseEntity<List<GreenhouseModel>> getAllGreenhouses() {
        List<GreenhouseModel> greenhouses = greenhouseService.getAllGreenhouses();
        return new ResponseEntity<>(greenhouses, HttpStatus.OK);
    }

    // Get greenhouse by ID
    @GetMapping("/greenhouse/{id}")
    public ResponseEntity<GreenhouseModel> getGreenhouseById(@PathVariable String id) {
        Optional<GreenhouseModel> greenhouse = greenhouseService.getGreenhouseById(id);
        return greenhouse.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a greenhouse
    @PutMapping("/admin/updategreenhouse/{id}")
    public ResponseEntity<GreenhouseModel> updateGreenhouse(@PathVariable String id, @Valid @RequestBody GreenhouseModel greenhouse) {
        GreenhouseModel updated = greenhouseService.updateGreenhouse(id, greenhouse);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Delete a greenhouse
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteGreenhouse(@PathVariable String id) {
        greenhouseService.deleteGreenhouse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search by name
    @GetMapping("/search")
    public ResponseEntity<List<GreenhouseModel>> searchByName(@RequestParam String name) {
        List<GreenhouseModel> greenhouses = greenhouseService.searchByName(name);
        return new ResponseEntity<>(greenhouses, HttpStatus.OK);
    }

    // Filter by price range
    @GetMapping("/filter/price")
    public ResponseEntity<List<GreenhouseModel>> filterByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {
        List<GreenhouseModel> greenhouses = greenhouseService.filterByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(greenhouses, HttpStatus.OK);
    }

    // Filter by material
    @GetMapping("/filter/material")
    public ResponseEntity<List<GreenhouseModel>> filterByMaterial(@RequestParam String material) {
        List<GreenhouseModel> greenhouses = greenhouseService.filterByMaterial(material);
        return new ResponseEntity<>(greenhouses, HttpStatus.OK);
    }

    // Filter by compatible crop
    @GetMapping("/filter/crop")
    public ResponseEntity<List<GreenhouseModel>> filterByCompatibleCrop(@RequestParam String crop) {
        List<GreenhouseModel> greenhouses = greenhouseService.filterByCompatibleCrop(crop);
        return new ResponseEntity<>(greenhouses, HttpStatus.OK);
    }

    // Handle exceptions
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}