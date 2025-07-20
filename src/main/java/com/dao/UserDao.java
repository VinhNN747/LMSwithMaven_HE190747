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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDao extends BaseDao<User> {

    public UserDao() {
        super();
    }

    public List<User> listUsers(List<Integer> userIds, Integer divisionId, Integer roleId, Integer managerId, Integer roleLevel, Integer pageNumber, Integer pageSize) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT u FROM User u WHERE 1=1");
            if (userIds != null) {
                jpql.append(" AND u.userId IN :userIds");
            }
            if (divisionId != null) {
                jpql.append(" AND u.divisionId = :divisionId");
            }
            if (roleId != null) {
                jpql.append(" AND u.roleId = :roleId");
            }
            if (managerId != null) {
                jpql.append(" AND u.managerId = :managerId");
            }
            if (roleLevel != null) {
                jpql.append(" AND u.role.roleLevel = :roleLevel");
            }
            var query = em.createQuery(jpql.toString(), User.class);
            if (userIds != null) {
                query.setParameter("userIds", userIds);
            }
            if (divisionId != null) {
                query.setParameter("divisionId", divisionId);
            }
            if (roleId != null) {
                query.setParameter("roleId", roleId);
            }
            if (managerId != null) {
                query.setParameter("managerId", managerId);
            }
            if (roleLevel != null) {
                query.setParameter("roleLevel", roleLevel);
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

    public long countUsers(List<Integer> userIds, Integer divisionId, Integer roleId, Integer managerId, Integer roleLevel) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(u) FROM User u WHERE 1=1");
            if (userIds != null) {
                jpql.append(" AND u.userId IN :userIds");
            }
            if (divisionId != null) {
                jpql.append(" AND u.divisionId = :divisionId");
            }
            if (roleId != null) {
                jpql.append(" AND u.roleId = :roleId");
            }
            if (managerId != null) {
                jpql.append(" AND u.managerId = :managerId");
            }
            if (roleLevel != null) {
                jpql.append(" AND u.role.roleLevel = :roleLevel");
            }
            var query = em.createQuery(jpql.toString(), Long.class);
            if (userIds != null) {
                query.setParameter("userIds", userIds);
            }
            if (divisionId != null) {
                query.setParameter("divisionId", divisionId);
            }
            if (roleId != null) {
                query.setParameter("roleId", roleId);
            }
            if (managerId != null) {
                query.setParameter("managerId", managerId);
            }
            if (roleLevel != null) {
                query.setParameter("roleLevel", roleLevel);
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> list() {
        return listUsers(null, null, null, null, null, null, null);
    }

    public List<Integer> getAllSubordinateIds(int userId) {
        EntityManager em = getEntityManager();
        try {
            // Get direct subordinates
            List<Integer> directSubordinates = em.createQuery(
                    "SELECT u.userId FROM User u WHERE u.managerId = :userId",
                    Integer.class)
                    .setParameter("userId", userId)
                    .getResultList();

            List<Integer> allSubordinates = new ArrayList<>(directSubordinates);

            // Recursively get subordinates of subordinates
            for (Integer subordinateId : directSubordinates) {
                allSubordinates.addAll(getAllSubordinateIds(subordinateId));
            }

            return allSubordinates;
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
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
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
                    .createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public User get(Integer id) {
        return getEntityManager().find(User.class, id);
    }

    public boolean existsById(Integer id) {
        return get(id) != null;
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

    public Role getUserRole(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            User user = em.find(User.class, userId);
            return user != null ? user.getRole() : null;
        } finally {
            em.close();
        }
    }

    public List<String> getUserFeatureEndpoints(Integer userId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT rf.feature.endpoint FROM RoleFeature rf WHERE rf.roleId = (SELECT u.roleId FROM User u WHERE u.userId = :userId) AND rf.feature.endpoint IS NOT NULL",
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

    public void clearUserRole(Integer userId) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setRoleId(null);
                user.setRole(null);
                em.merge(user);
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

    public List<User> getUsersByDivision(Integer divisionId) {
        return listUsers(null, divisionId, null, null, null, null, null);
    }
}
