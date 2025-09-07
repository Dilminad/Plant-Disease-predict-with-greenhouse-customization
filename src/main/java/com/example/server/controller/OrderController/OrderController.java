package com.example.server.controller.OrderController;

import com.example.server.model.Order.Order;
import com.example.server.model.Order.Order.OrderStatus;
import com.example.server.model.Order.Order.OrderType;
import com.example.server.model.Roles.Buyer;
import com.example.server.model.Roles.Farmer;
import com.example.server.model.Roles.SeedSeller;
import com.example.server.service.OrderService.OrderService;
import com.example.server.service.RolesServices.BuyerService;
import com.example.server.service.RolesServices.FarmerService;
import com.example.server.service.RolesServices.SeedSellerService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final FarmerService farmerService;
    private final BuyerService buyerService;
    private final SeedSellerService seedSellerService;

    @PostMapping("/auth/new/createorder")
    public ResponseEntity<?> createOrder(@RequestBody Order order, Principal principal) {
        try {
            String username = principal.getName();

            // SCENARIO 1: A Farmer is buying a Greenhouse or Seeds
            if (order.getType() == OrderType.GREENHOUSE || order.getType() == OrderType.SEED) {
                Farmer farmer = farmerService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Authenticated farmer not found with username: " + username));
                order.setBuyerId(farmer.getId());

                // For SEED orders, set the sellerId
                if (order.getType() == OrderType.SEED && order.getItemId() != null) {
                    List<SeedSeller> sellers = seedSellerService.getAllSeedSellers();
                    if (!sellers.isEmpty()) {
                        order.setSellerId(sellers.get(0).getId());
                    }
                }

            // SCENARIO 2: A Buyer is buying a Harvest product
            } else if (order.getType() == OrderType.HARVEST) {
                Buyer buyer = buyerService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Authenticated buyer not found with username: " + username));
                order.setBuyerId(buyer.getId());
                
            // SCENARIO 3: Handle unsupported types
            } else {
                return ResponseEntity.badRequest().body("Unsupported order type: " + order.getType());
            }

            // Validate required fields
            if (order.getBuyerId() == null) {
                return ResponseEntity.badRequest().body("Buyer ID is required");
            }
            
            if (order.getType() == OrderType.SEED && order.getSellerId() == null) {
                return ResponseEntity.badRequest().body("Seller ID is required for seed orders");
            }

            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An internal server error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/auth/orders/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Order>> getOrdersByBuyer(@PathVariable String buyerId) {
        try {
            List<Order> orders = orderService.getOrdersByBuyer(buyerId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Order>> getOrdersBySeller(@PathVariable String sellerId) {
        try {
            List<Order> orders = orderService.getOrdersBySeller(sellerId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // New endpoint to get total platform fees deducted for a seller
    @GetMapping("/auth/seller/{sellerId}/total-deductions")
    public ResponseEntity<Double> getTotalPlatformFeesBySeller(@PathVariable String sellerId) {
        try {
            double totalFees = orderService.getTotalPlatformFeesBySeller(sellerId);
            return ResponseEntity.ok(totalFees);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(0.0);
        }
    }

    @GetMapping("/auth/type/{type}")
    public ResponseEntity<List<Order>> getOrdersByType(@PathVariable OrderType type) {
        try {
            List<Order> orders = orderService.getOrdersByType(type);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/auth/getstype/{type}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByTypeAndStatus(
            @PathVariable OrderType type,
            @PathVariable OrderStatus status) {
        try {
            List<Order> orders = orderService.getOrdersByTypeAndStatus(type, status);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/auth/updates/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus status,
            @RequestParam(required = false) String notes) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status, notes);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/auth/greenhouseinstall/{orderId}/installation")
    public ResponseEntity<Order> updateGreenhouseInstallation(
            @PathVariable String orderId,
            @RequestBody Order.GreenhouseInstallationDetails installationDetails) {
        try {
            Order updatedOrder = orderService.updateGreenhouseInstallation(orderId, installationDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/auth/greenhouse/installations")
    public ResponseEntity<List<Order>> getUpcomingInstallations() {
        try {
            List<Order> orders = orderService.getUpcomingInstallations();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/auth/date-range")
    public ResponseEntity<List<Order>> getOrdersBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        try {
            List<Order> orders = orderService.getOrdersBetweenDates(start, end);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/admin/delete/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}