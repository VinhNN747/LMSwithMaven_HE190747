package com.dao;

import com.entity.Role;
import com.entity.Feature;
import com.entity.RoleFeature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RoleDao extends BaseDao<Role> {

    public RoleDao() {
        super();
    }

    @Override
    public List<Role> list() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Role r", Role.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void create(Role role) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(role);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void edit(Role role) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(role);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Role role) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Role managedRole = em.find(Role.class, role.getRoleId());
            if (managedRole != null) {
                em.remove(managedRole);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Role findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Role.class, id);
        } finally {
            em.close();
        }
    }

    public List<Feature> getFeaturesByRoleId(Integer roleId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                "SELECT rf.feature FROM RoleFeature rf WHERE rf.roleId = :roleId", 
                Feature.class)
                .setParameter("roleId", roleId)
                .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> getFeatureEndpointsByRoleId(Integer roleId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                "SELECT rf.feature.endpoint FROM RoleFeature rf WHERE rf.roleId = :roleId AND rf.feature.endpoint IS NOT NULL", 
                String.class)
                .setParameter("roleId", roleId)
                .getResultList();
        } finally {
            em.close();
        }
    }
} 