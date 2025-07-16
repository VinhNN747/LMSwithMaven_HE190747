package com.dao;

import com.entity.Role;
import com.entity.Feature;
import com.entity.RoleFeature;
import com.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class RoleDao extends BaseDao<Role> {

    public RoleDao() {
        super();
    }

    public List<Role> listRoles(String name, Integer pageNumber, Integer pageSize) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT r FROM Role r WHERE 1=1");
            if (name != null && !name.isEmpty()) {
                jpql.append(" AND r.roleName LIKE :name");
            }
            var query = em.createQuery(jpql.toString(), Role.class);
            if (name != null && !name.isEmpty()) {
                query.setParameter("name", "%" + name + "%");
            }
            if (pageNumber != null && pageSize != null) {
                query.setFirstResult((pageNumber - 1) * pageSize);
                query.setMaxResults(pageSize);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Role> list() {
        return listRoles(null, null, null);
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

    public Role get(Integer id) {
        return getEntityManager().createQuery("SELECT r FROM Role r WHERE r.roleId = :id", Role.class)
                .setParameter("id", id)
                .getSingleResult();
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

    public List<User> getUsersWithRoleId(Integer roleId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT u FROM User u WHERE u.roleId = :roleId",
                    User.class)
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

    public boolean hasPermission(Integer userId, String endpoint) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(rf) FROM RoleFeature rf WHERE rf.roleId = (SELECT u.roleId FROM User u WHERE u.userId = :userId) AND rf.feature.endpoint = :endpoint",
                    Long.class)
                    .setParameter("userId", userId)
                    .setParameter("endpoint", endpoint)
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

    public long countRoles(String name) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(r) FROM Role r WHERE 1=1");
            if (name != null && !name.isEmpty()) {
                jpql.append(" AND r.roleName LIKE :name");
            }
            var query = em.createQuery(jpql.toString(), Long.class);
            if (name != null && !name.isEmpty()) {
                query.setParameter("name", "%" + name + "%");
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
