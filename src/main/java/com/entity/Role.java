package com.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer roleId;

    @Column(name = "RoleName", nullable = false, length = 50)
    private String roleName;

    @Column(name = "RoleDescription", length = 50)
    private String roleDescription;

    @Column(name = "RoleLevel")
    private Integer roleLevel;

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    // Navigation property for UserRole
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    // Navigation property for RoleFeature
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleFeature> roleFeatures = new ArrayList<>();

    // Getters and Setters
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public List<RoleFeature> getRoleFeatures() {
        return roleFeatures;
    }

    public void setRoleFeatures(List<RoleFeature> roleFeatures) {
        this.roleFeatures = roleFeatures;
    }

}
