package com.dao;

import com.entity.Division;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class DivisionDao extends BaseDao<Division> {

    public DivisionDao() {
        super();
    }

    public Division get(int id) {
        return getEntityManager().find(Division.class, id);
    }

    public List<Division> list(String name, Integer pageNumber, Integer pageSize) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT d FROM Division d WHERE 1=1");
            if (name != null && !name.isEmpty()) {
                jpql.append(" AND d.divisionName LIKE :name");
            }
            var query = em.createQuery(jpql.toString(), Division.class);
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
    public List<Division> list() {
        return list(null, null, null);
    }

    @Override
    public void create(Division division) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(division);
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
    public void edit(Division division) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(division);
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
    public void delete(Division division) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (division != null) {
                Division managedDivision = em.merge(division);
                em.remove(managedDivision);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }
    }

    public boolean existsByName(String name) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(d) FROM Division d WHERE d.divisionName = :name", Long.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<Division> getActiveDivisions() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT d FROM Division d WHERE d.divisionId IN "
                    + "(SELECT DISTINCT u.divisionId FROM User u WHERE u.isActive = true)", Division.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public long countUsers(String name) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT COUNT(d) FROM Division d WHERE 1=1");
            if (name != null && !name.isEmpty()) {
                jpql.append(" AND d.divisionName LIKE :name");
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
