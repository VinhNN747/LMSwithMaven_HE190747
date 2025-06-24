package com.entity;

import java.io.Serializable;
import java.util.Objects;

public class RoleFeatureId implements Serializable {
    private Integer roleId;
    private Integer featureId;

    public RoleFeatureId() {}
    public RoleFeatureId(Integer roleId, Integer featureId) {
        this.roleId = roleId;
        this.featureId = featureId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleFeatureId that = (RoleFeatureId) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(featureId, that.featureId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(roleId, featureId);
    }
} 