package com.example.server.model.Roles;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "buyers")
public class Buyer extends User {
    private List<String> orderHistory;
    private List<String> favoriteFarmers;
    
    @Override
    public String getRole() { 
        return "BUYER"; 
    }
}