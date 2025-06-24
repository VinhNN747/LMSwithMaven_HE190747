package com.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Role_Feature")
@IdClass(RoleFeatureId.class)
public class RoleFeature implements Serializable {
    @Id
    @Column(name = "RoleID")
    private Integer roleId;

    @Id
    @Column(name = "FeatureID")
    private Integer featureId;

    @ManyToOne
    @JoinColumn(name = "RoleID", insertable = false, updatable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "FeatureID", insertable = false, updatable = false)
    private Feature feature;

    // Getters and Setters
    public Integer getRoleId() { return roleId; }
    public void setRoleId(Integer roleId) { this.roleId = roleId; }
    public Integer getFeatureId() { return featureId; }
    public void setFeatureId(Integer featureId) { this.featureId = featureId; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Feature getFeature() { return feature; }
    public void setFeature(Feature feature) { this.feature = feature; }
} 