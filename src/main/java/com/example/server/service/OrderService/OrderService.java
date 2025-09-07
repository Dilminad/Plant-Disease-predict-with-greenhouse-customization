package com.example.server.service.OrderService;



import com.example.server.model.Order.Order;
import com.example.server.model.Order.Order.OrderStatus;
import com.example.server.model.Order.Order.OrderType;
import com.example.server.model.Order.Order.PaymentStatus;
import com.example.server.model.Products.Harvest;
import com.example.server.model.Products.SeedListing;
import com.example.server.service.Seed_and_Harvest_ListingService.HarvestService;
import com.example.server.service.Seed_and_Harvest_ListingService.SeedListingService;
import com.example.server.service.RolesServices.FarmerService;
import com.example.server.service.PlatformSettings.PlatformSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.server.repository.OrderRepo.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final HarvestService harvestService;
    private final SeedListingService seedListingService;
    private final FarmerService farmerService;
    private final PlatformSettingsService platformSettingsService;

    @Transactional
    public Order createOrder(Order order) {
        // Set basic order details
        order.setOrderNumber(generateOrderNumber(order.getType()));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

        // Calculate subtotal (what buyer pays for products)
        double subtotal = order.getQuantity() * order.getUnitPrice();
        
        // Calculate platform fee using PlatformSettingsService
        double platformFee = platformSettingsService.calculatePlatformFee(subtotal);
        order.setPlatformFee(platformFee);

        // Calculate seller's receivable amount (what seller actually gets)
        double sellerReceivableAmount = subtotal - platformFee;
        order.setSellerReceivableAmount(sellerReceivableAmount);

        // Set totalPrice (what the buyer pays: subtotal + delivery fee)
        double totalPrice = subtotal + (order.getDeliveryFee() > 0 ? order.getDeliveryFee() : 0);
        order.setTotalPrice(totalPrice);

        // Initialize status history
        Order.StatusHistory initialStatus = new Order.StatusHistory(
                OrderStatus.PENDING,
                LocalDateTime.now(),
                "Order created"
        );
        order.setStatusHistory(List.of(initialStatus));

        // Logic to update harvest product quantity
        if (order.getType() == OrderType.HARVEST) {
            String harvestId = order.getItemId();
            double orderedQuantity = order.getQuantity();

            Harvest harvest = harvestService.getHarvestById(harvestId)
                    .orElseThrow(() -> new RuntimeException("Harvest product not found with ID: " + harvestId));

            if (harvest.getQuantityAvailable() < orderedQuantity) {
                throw new IllegalStateException(
                        "Insufficient stock for product '" + harvest.getProductName() + "'. Requested: " + orderedQuantity + ", Available: " + harvest.getQuantityAvailable()
                );
            }

            double newQuantityAvailable = harvest.getQuantityAvailable() - orderedQuantity;
            harvest.setQuantityAvailable(newQuantityAvailable);
            harvestService.updateHarvest(harvestId, harvest);

        // Logic to update seed stock
        } else if (order.getType() == OrderType.SEED) {
            String seedId = order.getItemId();
            int orderedQuantity = (int) order.getQuantity();

            SeedListing seed = seedListingService.getSeedListingById(seedId)
                    .orElseThrow(() -> new RuntimeException("Seed product not found with ID: " + seedId));

            if (seed.getAvailableQuantity() < orderedQuantity) {
                throw new IllegalStateException(
                        "Insufficient stock for product '" + seed.getName() + "'. Requested: " + orderedQuantity + ", Available: " + seed.getAvailableQuantity()
                );
            }

            // Update stock quantity
            seedListingService.updateStockQuantity(seedId, -orderedQuantity);
        }

        return orderRepository.save(order);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
    }

    @Transactional
    public Order updateOrderStatus(String orderId, OrderStatus newStatus, String notes) {
        Order order = getOrderById(orderId);

        Order.StatusHistory historyEntry = new Order.StatusHistory(
                newStatus,
                LocalDateTime.now(),
                notes
        );
        
        if (order.getStatusHistory() == null) {
            order.setStatusHistory(new ArrayList<>());
        }
        order.getStatusHistory().add(historyEntry);
        order.setStatus(newStatus);

        if (newStatus == OrderStatus.DELIVERED) {
            order.setActualDeliveryDate(LocalDateTime.now());
            if (order.getType() == OrderType.GREENHOUSE) {
                System.out.println(">>> Greenhouse order " + order.getId() + " marked as DELIVERED. Starting farmer update process.");
                
                String greenhouseId = order.getItemId();
                String farmerId = order.getBuyerId();
                
                if (farmerId != null && greenhouseId != null) {
                    System.out.println(">>> Found Farmer ID: " + farmerId + " and Greenhouse ID: " + greenhouseId);
                    System.out.println(">>> Calling FarmerService to add greenhouse to farmer...");
                    
                    farmerService.addGreenhouseToFarmer(farmerId, greenhouseId);
                    
                    System.out.println(">>> FarmerService call completed.");
                } else {
                    System.out.println(">>> ERROR: Farmer ID or Greenhouse ID is null. Cannot update farmer.");
                }
            }
        }

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(String orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Transactional
    public Order updateGreenhouseInstallation(String orderId, Order.GreenhouseInstallationDetails installationDetails) {
        Order order = getOrderById(orderId);

        if (order.getType() != OrderType.GREENHOUSE) {
            throw new RuntimeException("Installation details can only be updated for GREENHOUSE orders");
        }

        order.setInstallationDetails(installationDetails);
        if ("COMPLETED".equals(installationDetails.getInstallationStatus())) {
            String greenhouseId = order.getItemId();
            String farmerId = order.getBuyerId();
            if (farmerId != null && greenhouseId != null) {
                farmerService.addGreenhouseToFarmer(farmerId, greenhouseId);
            }
        }

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByBuyer(String buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    public List<Order> getOrdersBySeller(String sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    public List<Order> getOrdersByType(OrderType type) {
        return orderRepository.findByType(type);
    }

    public List<Order> getOrdersByTypeAndStatus(OrderType type, OrderStatus status) {
        return orderRepository.findByTypeAndStatus(type, status);
    }

    public List<Order> getUpcomingInstallations() {
        return orderRepository.findUpcomingGreenhouseInstallations(LocalDateTime.now());
    }

    public List<Order> getOrdersBetweenDates(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findOrdersBetweenDates(start, end);
    }

    // New method to calculate total platform fees deducted for a seller
    public double getTotalPlatformFeesBySeller(String sellerId) {
        List<Order> orders = orderRepository.findBySellerId(sellerId);
        return orders.stream()
                .mapToDouble(Order::getPlatformFee)
                .sum();
    }

    private String generateOrderNumber(OrderType type) {
        return String.format("%s-%d-%s",
                type.name(),
                LocalDateTime.now().getYear(),
                UUID.randomUUID().toString().substring(0, 8).toUpperCase()
        );
    }

    public double calculatePlatformFeeForOrder(double subtotal) {
        return platformSettingsService.calculatePlatformFee(subtotal);
    }
}