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

    @Override
    public List<Division> list() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT d FROM Division d", Division.class).getResultList();
        } finally {
            em.close();
        }
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
}
