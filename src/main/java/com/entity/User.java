/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "[User]")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Integer userId;

    @NotNull
    @Size(max = 50)
    @Column(name = "FullName", nullable = false)
    private String fullName;

    @NotNull
    @Size(max = 50)
    @Column(name = "Username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Size(max = 255)
    @Column(name = "Password", nullable = false)
    private String password;

    @NotNull
    @Email
    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "DivisionID")
    private Integer divisionId;

    @Column(name = "Gender")
    private Boolean gender;

    @Column(name = "ManagerID")
    private Integer managerId;

    @Column(name = "RoleID")
    private Integer roleId;

    // Navigation property for the division
    @ManyToOne
    @JoinColumn(name = "DivisionID", insertable = false, updatable = false)
    private Division division;

    // Navigation property for the manager
    @ManyToOne
    @JoinColumn(name = "ManagerID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private User manager;

    @ManyToOne
    @JoinColumn(name = "RoleID", insertable = false, updatable = false)
    private Role role;

    // Navigation property for employees managed by this user
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    private List<User> managedUsers = new ArrayList<>();

    // Navigation property for leave requests sent by this user
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<LeaveRequest> sentLeaveRequests = new ArrayList<>();

    // Navigation property for leave requests to be approved by this user
    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
    private List<LeaveRequest> receivedLeaveRequests = new ArrayList<>();

    // Getters and Setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    public Integer getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) {
        this.divisionId = divisionId;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Hibernate requires a no-arg constructor
    public User() {
    }

}
