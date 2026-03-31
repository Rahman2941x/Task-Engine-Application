package com.taskengine.taskengine_user_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "id",unique = true)
    private Long id;
    @Column(unique = true,name = "user_name")
    private String userName;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false,name = "password")
    private String password;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "alternate_mobile_number")
    private String alternativeNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @PrePersist
    public  void PreUpdate(){
    this.createdAt=LocalDateTime.now();
    this.updatedAt=LocalDateTime.now();
    this.isActive=true;
    }

    @PreUpdate
    public  void PostUpdate(){
        this.updatedAt=LocalDateTime.now();
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAlternativeNumber() {
        return alternativeNumber;
    }

    public void setAlternativeNumber(String alternativeNumber) {
        this.alternativeNumber = alternativeNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", alternativeNumber='" + alternativeNumber + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public User(Long id, String email, String userName, String password, String mobileNumber, String alternativeNumber, Role role, LocalDateTime createdAt, Boolean isActive, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.alternativeNumber = alternativeNumber;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}