package com.example.server.model.Roles;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "users")
public abstract class User {
    @Id
    protected String id;
    protected String username;
    protected String password;
    protected String email;
    protected String phone;
    protected boolean active = true;
    protected LocalDateTime createdAt = LocalDateTime.now();
    protected String street;
    protected String city;
    protected String state;
    protected String zipCode;
    protected String country;
    protected String profileImageUrl;

    public abstract String getRole();
}