/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dao;

import com.entity.User;
import com.entity.Division;
import com.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Objects;

public class UserDao extends BaseDao<User> {

    public UserDao() {
        super();
    }

    @Override
    public List<User> list() {
        EntityManager em = getEntityManager();
        try {
            return em
                    .createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role ORDER BY u.fullName",
                            User.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get paginated list of users
     */
    public List<User> listPaginated(int page, int pageSize) {
        EntityManager em = getEntityManager();
        try {
            return em
                    .createQuery("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userRoles ur LEFT JOIN FETCH ur.role ORDER BY u.fullName",
                            User.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get total count of users
     */
    public long getTotalCount() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public void create(User user) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
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
    public void edit(User user) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(user);
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
    public void delete(User user) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (user != null) {
                User managedUser = em.find(User.class, user.getUserId());
                if (managedUser == null) {
                    throw new RuntimeException("User not found in database");
                }

                // Handle division head removal if this user is a division head
                if (managedUser.getDivisionId() != null) {
                    Division division = em.find(Division.class, managedUser.getDivisionId());
                    if (division != null && Objects.equals(division.getDivisionHead(), managedUser.getUserId())) {
                        // This user is a division head, remove them from the division
                        division.setDivisionHead(null);
                        em.merge(division);
                    }
                }

                // Update managerId references in other users to point to division head or null
                if (managedUser.getDivisionId() != null) {
                    Division division = em.find(Division.class, managedUser.getDivisionId());
                    Integer newManagerId = (division != null) ? division.getDivisionHead() : null;

                    em.createQuery("UPDATE User u SET u.managerId = :newManagerId "
                            + "WHERE u.managerId = :userId "
                            + "AND u.divisionId = :divisionId")
                            .setParameter("newManagerId", newManagerId)
                            .setParameter("userId", managedUser.getUserId())
                            .setParameter("divisionId", managedUser.getDivisionId())
                            .executeUpdate();
                }

                // Remove the user
                em.remove(managedUser);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to delete user: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public boolean existsByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE LOWER(u.email) = LOWER(:email)", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public boolean existsByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            Long count = em
                    .createQuery("SELECT COUNT(u) FROM User u WHERE LOWER(u.username) = LOWER(:username)", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public User getById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public boolean existsById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.userId = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public boolean existsByDivisionId(Integer divisionId) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(d) FROM Division d WHERE d.divisionId = :divisionId", Long.class)
                    .setParameter("divisionId", divisionId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public User getByUsernameAndPassword(String username, String password) {
        EntityManager em = getEntityManager();
        try {
            List<User> users = em
                    .createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password",
                            User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getResultList();
            return users.isEmpty() ? null : users.get(0);
        } finally {
            em.close();
        }
    }

    public List<Role> getUserRoles(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT ur.role FROM UserRole ur WHERE ur.userId = :userId",
                    Role.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<String> getUserFeatureEndpoints(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT rf.feature.endpoint FROM UserRole ur "
                    + "JOIN RoleFeature rf ON ur.roleId = rf.roleId "
                    + "WHERE ur.userId = :userId AND rf.feature.endpoint IS NOT NULL",
                    String.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean hasPermission(Integer userId, String endpoint) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(rf) FROM UserRole ur "
                    + "JOIN RoleFeature rf ON ur.roleId = rf.roleId "
                    + "WHERE ur.userId = :userId AND rf.feature.endpoint = :endpoint",
                    Long.class)
                    .setParameter("userId", userId)
                    .setParameter("endpoint", endpoint)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<String> getUserRoleNames(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT r.roleName "
                    + "FROM UserRole ur "
                    + "JOIN ur.role r "
                    + "WHERE ur.userId = :userId",
                    String.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
