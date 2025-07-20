package com.dao;

import com.entity.LeaveRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.Date;
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
        return listRequests(null, null, null, null, null, null, null, null);
    }

    public List<LeaveRequest> listRequests(List<Integer> senderIds, String status, Integer reviewerId, Integer divisionId,
            Integer pageNumber, Integer pageSize, Date from, Date to) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT lr FROM LeaveRequest lr WHERE 1=1");
            if (senderIds != null) {
                jpql.append(" AND lr.senderId IN :senderIds");
            }
            if (status != null && !status.isEmpty()) {
                jpql.append(" AND lr.status = :status");
            }
            if (reviewerId != null) {
                jpql.append(" AND lr.reviewerId = :reviewerId");
            }
            if (divisionId != null) {
                jpql.append(" AND lr.sender.divisionId = :divisionId");
            }
            if (from != null && to != null) {
                jpql.append(" AND lr.endDate >= :from AND lr.startDate <= :to");
            }
            var query = em.createQuery(jpql.toString(), LeaveRequest.class);
            if (senderIds != null) {
                query.setParameter("senderIds", senderIds);
            }
            if (status != null && !status.isEmpty()) {
                query.setParameter("status", status);
            }
            if (reviewerId != null) {
                query.setParameter("reviewerId", reviewerId);
            }
            if (divisionId != null) {
                query.setParameter("divisionId", divisionId);
            }
            if (from != null && to != null) {
                query.setParameter("from", from);
                query.setParameter("to", to);
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

    public long countRequests(List<Integer> senderIds, String status, Integer reviewerId, Integer divisionId) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(lr) FROM LeaveRequest lr WHERE 1=1");
            if (senderIds != null) {
                jpql.append(" AND lr.senderId IN :senderIds");
            }
            if (status != null && !status.isEmpty()) {
                jpql.append(" AND lr.status = :status");
            }
            if (reviewerId != null) {
                jpql.append(" AND lr.reviewerId = :reviewerId");
            }
            if (divisionId != null) {
                jpql.append(" AND lr.sender.divisionId = :divisionId");
            }
            var query = em.createQuery(jpql.toString(), Long.class);
            if (senderIds != null) {
                query.setParameter("senderIds", senderIds);
            }
            if (status != null && !status.isEmpty()) {
                query.setParameter("status", status);
            }
            if (reviewerId != null) {
                query.setParameter("reviewerId", reviewerId);
            }
            if (divisionId != null) {
                query.setParameter("divisionId", divisionId);
            }
            return query.getSingleResult();
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
    public LeaveRequest get(int id) {
        return getEntityManager().find(LeaveRequest.class, id);
    }
}
