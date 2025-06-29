/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dao;

import com.entity.LeaveRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vinhnnpc
 */
public class LeaveRequestDao extends BaseDao<LeaveRequest> {

    public LeaveRequestDao() {
        super();
    }

    @Override
    public List<LeaveRequest> list() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT lr FROM LeaveRequest lr ORDER BY lr.startDate DESC", LeaveRequest.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get paginated list of all leave requests
     */
    public List<LeaveRequest> listPaginated(int page, int pageSize) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT lr FROM LeaveRequest lr LEFT JOIN FETCH lr.sender LEFT JOIN FETCH lr.reviewer ORDER BY lr.startDate DESC", LeaveRequest.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get total count of all leave requests
     */
    public long getTotalCount() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(lr) FROM LeaveRequest lr", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<LeaveRequest> listOf(int userId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT lr FROM LeaveRequest lr LEFT JOIN FETCH lr.reviewer WHERE lr.senderId = :userId ORDER BY lr.startDate DESC",
                    LeaveRequest.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get paginated list of user's leave requests
     */
    public List<LeaveRequest> listOfPaginated(int userId, int page, int pageSize) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT lr FROM LeaveRequest lr LEFT JOIN FETCH lr.reviewer WHERE lr.senderId = :userId ORDER BY lr.startDate DESC",
                    LeaveRequest.class)
                    .setParameter("userId", userId)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Get total count of user's leave requests
     */
    public long getTotalCountForUser(int userId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(lr) FROM LeaveRequest lr WHERE lr.senderId = :userId", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<LeaveRequest> leaveRequestsOfSubs(int userId) {
        EntityManager em = getEntityManager();
        try {
            // First, get all subordinate IDs using a recursive approach
            List<Integer> subordinateIds = getAllSubordinateIds(userId);

            if (subordinateIds.isEmpty()) {
                return new ArrayList<>();
            }

            // Then get leave requests from all subordinates with proper fetching
            return em.createQuery(
                    "SELECT lr FROM LeaveRequest lr "
                    + "LEFT JOIN FETCH lr.reviewer "
                    + "LEFT JOIN FETCH lr.sender "
                    + "WHERE lr.senderId IN :subordinateIds "
                    + "ORDER BY lr.startDate DESC",
                    LeaveRequest.class)
                    .setParameter("subordinateIds", subordinateIds)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Error in leaveRequestsOfSubs: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Get paginated list of subordinates' leave requests
     */
    public List<LeaveRequest> leaveRequestsOfSubsPaginated(int userId, int page, int pageSize) {
        EntityManager em = getEntityManager();
        try {
            // First, get all subordinate IDs using a recursive approach
            List<Integer> subordinateIds = getAllSubordinateIds(userId);

            if (subordinateIds.isEmpty()) {
                return new ArrayList<>();
            }

            // Then get leave requests from all subordinates with proper fetching
            return em.createQuery(
                    "SELECT lr FROM LeaveRequest lr "
                    + "LEFT JOIN FETCH lr.reviewer "
                    + "LEFT JOIN FETCH lr.sender "
                    + "WHERE lr.senderId IN :subordinateIds "
                    + "ORDER BY lr.startDate DESC",
                    LeaveRequest.class)
                    .setParameter("subordinateIds", subordinateIds)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Error in leaveRequestsOfSubsPaginated: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Get total count of subordinates' leave requests
     */
    public long getTotalCountForSubs(int userId) {
        EntityManager em = getEntityManager();
        try {
            List<Integer> subordinateIds = getAllSubordinateIds(userId);

            if (subordinateIds.isEmpty()) {
                return 0;
            }

            return em.createQuery("SELECT COUNT(lr) FROM LeaveRequest lr WHERE lr.senderId IN :subordinateIds", Long.class)
                    .setParameter("subordinateIds", subordinateIds)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("Error in getTotalCountForSubs: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Helper method to get all subordinate IDs recursively
     */
    private List<Integer> getAllSubordinateIds(int userId) {
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
    public void create(LeaveRequest lr) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(lr);
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
    public void edit(LeaveRequest lr) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(lr);
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
    public void delete(LeaveRequest lr) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            LeaveRequest managedLr = em.find(LeaveRequest.class, lr.getLeaveRequestId());
            if (managedLr != null) {
                em.remove(managedLr);
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

    /**
     * Find a LeaveRequest by its ID
     */
    public LeaveRequest find(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LeaveRequest.class, id);
        } finally {
            em.close();
        }
    }

}
