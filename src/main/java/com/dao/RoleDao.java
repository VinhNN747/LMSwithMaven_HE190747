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
            return em.createQuery("SELECT r FROM Role r LEFT JOIN FETCH r.roleFeatures rf LEFT JOIN FETCH rf.feature ORDER BY r.roleName", Role.class).getResultList();
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
        System.out.println("delete");
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Role managedRole = em.find(Role.class, role.getRoleId());
            if (managedRole != null) {
                em.remove(managedRole);
                System.out.println("delete");
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
            return em.createQuery("SELECT r FROM Role r LEFT JOIN FETCH r.userRoles WHERE r.roleId = :id", Role.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
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

    public boolean existsByName(String roleName, Integer excludeRoleId) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM Role r WHERE r.roleName = :roleName";
            if (excludeRoleId != null) {
                jpql += " AND r.roleId != :excludeRoleId";
            }

            var query = em.createQuery(jpql, Long.class)
                    .setParameter("roleName", roleName);

            if (excludeRoleId != null) {
                query.setParameter("excludeRoleId", excludeRoleId);
            }

            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public boolean existsByName(String roleName) {
        return existsByName(roleName, null);
    }

    public boolean hasPermission(Integer userId, String permission) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(rf) FROM RoleFeature rf "
                    + "JOIN rf.role r "
                    + "JOIN r.userRoles ur "
                    + "WHERE ur.userId = :userId AND rf.feature.endpoint = :permission",
                    Long.class)
                    .setParameter("userId", userId)
                    .setParameter("permission", permission)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public void clearRoleFeatures(Integer roleId) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("DELETE FROM RoleFeature rf WHERE rf.roleId = :roleId")
                    .setParameter("roleId", roleId)
                    .executeUpdate();
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

    public void addRoleFeature(Integer roleId, Integer featureId) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            RoleFeature roleFeature = new RoleFeature();
            roleFeature.setRoleId(roleId);
            roleFeature.setFeatureId(featureId);
            em.persist(roleFeature);
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

    public Role findByName(String roleName) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT r FROM Role r WHERE r.roleName = :roleName", Role.class)
                    .setParameter("roleName", roleName)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Integer getRoleIdByName(String roleName) {
        Role role = findByName(roleName);
        return role != null ? role.getRoleId() : null;
    }
}
