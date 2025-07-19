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
@Document(collection = "admins")
public class Admin extends User {
    private String department;
    private List<String> permissions;
    
    @Override
    public String getRole() { 
        return "ADMIN"; 
    }
}