package com.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Feature")
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeatureID")
    private Integer featureId;

    @Column(name = "FeatureName")
    private String featureName;

    @Column(name = "Endpoint")
    private String endpoint;

    // Navigation property for RoleFeature
    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL)
    private List<RoleFeature> roleFeatures = new ArrayList<>();

    // Getters and Setters
    public Integer getFeatureId() { return featureId; }
    public void setFeatureId(Integer featureId) { this.featureId = featureId; }
    public String getFeatureName() { return featureName; }
    public void setFeatureName(String featureName) { this.featureName = featureName; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public List<RoleFeature> getRoleFeatures() { return roleFeatures; }
    public void setRoleFeatures(List<RoleFeature> roleFeatures) { this.roleFeatures = roleFeatures; }
} 