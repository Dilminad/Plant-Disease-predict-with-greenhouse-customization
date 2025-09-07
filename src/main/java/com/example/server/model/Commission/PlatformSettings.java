package com.example.server.model.Commission;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "platform_settings")
public class PlatformSettings {
    @Id
    private String id ; // Single document ID
    
    private double platformFeePercentage;    // Percentage-based fee (e.g., 5.0 for 5%)
    private double fixedPlatformFee;         // Fixed amount fee (e.g., 2.50 for $2.50)
    
    private String feeCalculationMethod;     // "PERCENTAGE" or "FIXED"
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}