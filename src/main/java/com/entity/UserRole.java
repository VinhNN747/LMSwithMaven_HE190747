package com.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "User_Role")
@IdClass(UserRoleId.class)
public class UserRole implements Serializable {
    @Id
    @Column(name = "UserID")
    private Integer userId;

    @Id
    @Column(name = "RoleID")
    private Integer roleId;

    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "RoleID", insertable = false, updatable = false)
    private Role role;

    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
} 