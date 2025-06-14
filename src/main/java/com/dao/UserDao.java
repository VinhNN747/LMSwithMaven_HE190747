/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dao;

import com.entity.User;
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
                // Nullify managerId references in other users
                em.createQuery("UPDATE User u SET u.managerId = NULL WHERE u.managerId = :userId")
                        .setParameter("userId", user.getUserId())
                        .executeUpdate();

                // Nullify divisionDirector in Division if this user is a director
                em.createQuery("UPDATE Division d SET d.divisionDirector = NULL WHERE d.divisionDirector = :userId")
                        .setParameter("userId", user.getUserId())
                        .executeUpdate();

                // Merge and remove the user
                User managedUser = em.merge(user);
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

    public boolean existsByUsernameOrEmail(String username, String email) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE LOWER(u.username) = LOWER(:username) OR LOWER(u.email) = LOWER(:email)", Long.class)
                    .setParameter("username", username)
                    .setParameter("email", email)
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
}
