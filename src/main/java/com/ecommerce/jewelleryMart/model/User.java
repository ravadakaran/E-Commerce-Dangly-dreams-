package com.ecommerce.jewelleryMart.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;
    private String username;
    private String email;
    private String password;
    private String profilePic;

    /** Role: ADMIN or USER */
    private String role = "USER";

    @JsonProperty("isAdmin")
    private boolean isAdmin = false;

    private Date createdAt;

    public User() {
        this.createdAt = new Date();
    }

    public User(String username, String email, String password, String profilePic, boolean isAdmin) {
        this.username = username;
        this.name = username;
        this.email = email;
        this.password = password;
        this.profilePic = profilePic;
        this.isAdmin = isAdmin;
        this.role = isAdmin ? "ADMIN" : "USER";
        this.createdAt = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        this.isAdmin = "ADMIN".equalsIgnoreCase(role);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // -------- Getters & Setters --------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    // ✅ CORRECT admin getter/setter
    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
        this.role = admin ? "ADMIN" : "USER";
    }
}
