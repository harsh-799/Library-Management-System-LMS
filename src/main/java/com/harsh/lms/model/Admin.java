package com.harsh.lms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Admin {
    @Id
    private String username;
    private String password;
    private final String role = "admin";

    public Admin(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
}