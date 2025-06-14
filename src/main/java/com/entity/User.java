/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "[User]")
public class User {

    @Id
    @Size(max = 10)
    @Column(name = "UserID")
    private String userId;

    @NotNull
    @Size(max = 100)
    @Column(name = "FullName", nullable = false)
    private String fullName;

    @NotNull
    @Size(max = 50)
    @Column(name = "Username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Size(max = 100)
    @Email
    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^[MF]$", message = "Gender must be 'M' or 'F'")
    @Size(max = 1)
    @Column(name = "Gender", length = 1)
    private String gender;

    @Column(name = "DivisionID")
    private Integer divisionId;

    @NotNull
    @Size(max = 100)
    @Column(name = "Role", nullable = false)
    private String role = "Employee";

    @NotNull
    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = true;

    @Size(max = 10)
    @Column(name = "ManagerID")
    private String managerId;

    // Navigation property for the division
    @ManyToOne
    @JoinColumn(name = "DivisionID", insertable = false, updatable = false)
    private Division division;

    // Navigation property for the manager
    @ManyToOne
    @JoinColumn(name = "ManagerID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private User manager;

    // Navigation property for employees managed by this user
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<User> managedUsers = new ArrayList<>();

    // Navigation property for leave requests sent by this user
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<LeaveRequest> sentLeaveRequests = new ArrayList<>();

    // Navigation property for leave requests to be approved by this user
    @OneToMany(mappedBy = "approver", cascade = CascadeType.ALL)
    private List<LeaveRequest> receivedLeaveRequests = new ArrayList<>();

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public List<User> getManagedUsers() {
        return managedUsers;
    }

    public void setManagedUsers(List<User> managedUsers) {
        this.managedUsers = managedUsers;
    }

    public List<LeaveRequest> getSentLeaveRequests() {
        return sentLeaveRequests;
    }

    public void setSentLeaveRequests(List<LeaveRequest> sentLeaveRequests) {
        this.sentLeaveRequests = sentLeaveRequests;
    }

    public List<LeaveRequest> getReceivedLeaveRequests() {
        return receivedLeaveRequests;
    }

    public void setReceivedLeaveRequests(List<LeaveRequest> receivedLeaveRequests) {
        this.receivedLeaveRequests = receivedLeaveRequests;
    }

    // Hibernate requires a no-arg constructor
    public User() {
    }

    // Constructor for convenience
    public User(String userId, String fullName, String username, String email, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
