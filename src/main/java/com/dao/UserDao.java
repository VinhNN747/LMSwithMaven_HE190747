/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dao;

import com.entity.User;
import com.entity.Division;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class UserDao extends BaseDao<User> {

    public UserDao() {
        super();
    }

    @Override
    public List<User> list() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u", User.class).getResultList();
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

                // Get the division director before nullifying references
                String divisionDirector = null;
                if (managedUser.getDivisionId() != null) {
                    Division division = em.find(Division.class, managedUser.getDivisionId());
                    if (division != null) {
                        divisionDirector = division.getDivisionHead();
                    }
                }

                // Update managerId references in other users to point to division director
                em.createQuery("UPDATE User u SET u.managerId = :divisionDirector "
                        + "WHERE u.managerId = :userId "
                        + "AND u.divisionId = :divisionId")
                        .setParameter("divisionDirector", divisionDirector)
                        .setParameter("userId", managedUser.getUserId())
                        .setParameter("divisionId", managedUser.getDivisionId())
                        .executeUpdate();

                // Nullify divisionDirector in Division if this user is a director
                em.createQuery("UPDATE Division d SET d.divisionDirector = NULL WHERE d.divisionDirector = :userId")
                        .setParameter("userId", managedUser.getUserId())
                        .executeUpdate();

                // Merge and remove the user
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
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE LOWER(u.username) = LOWER(:username)", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public User findById(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public boolean existsById(String id) {
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

    public int getMaxIndexForAcronym(String acronym) {
        EntityManager em = getEntityManager();
        try {
            // Query to get all UserIDs starting with the acronym
            List<String> userIds = em.createQuery("SELECT u.userId FROM User u WHERE UPPER(u.userId) LIKE :acronymPattern", String.class)
                    .setParameter("acronymPattern", acronym.toUpperCase() + "%")
                    .getResultList();

            // Extract the numeric suffix and find the maximum
            int maxIndex = 0;
            for (String userId : userIds) {
                // Ensure userId is long enough to have a numeric suffix
                if (userId.length() > acronym.length()) {
                    try {
                        String suffix = userId.substring(acronym.length());
                        int index = Integer.parseInt(suffix);
                        maxIndex = Math.max(maxIndex, index);
                    } catch (NumberFormatException e) {
                        // Skip invalid suffixes
                        continue;
                    }
                }
            }
            return maxIndex;
        } finally {
            em.close();
        }
    }
}
