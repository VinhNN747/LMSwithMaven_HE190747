package com.dao;

import com.entity.Feature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class FeatureDao extends BaseDao<Feature> {

    public FeatureDao() {
        super();
    }

    @Override
    public List<Feature> list() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT f FROM Feature f ORDER BY f.featureName", Feature.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void create(Feature feature) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(feature);
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
    public void edit(Feature feature) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(feature);
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
    public void delete(Feature feature) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Feature managedFeature = em.find(Feature.class, feature.getFeatureId());
            if (managedFeature != null) {
                em.remove(managedFeature);
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

    public Feature findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Feature.class, id);
        } finally {
            em.close();
        }
    }

    public boolean existsByName(String featureName, Integer excludeFeatureId) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(f) FROM Feature f WHERE f.featureName = :featureName";
            if (excludeFeatureId != null) {
                jpql += " AND f.featureId != :excludeFeatureId";
            }
            
            var query = em.createQuery(jpql, Long.class)
                    .setParameter("featureName", featureName);
            
            if (excludeFeatureId != null) {
                query.setParameter("excludeFeatureId", excludeFeatureId);
            }
            
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    public boolean existsByName(String featureName) {
        return existsByName(featureName, null);
    }

    public Feature findByEndpoint(String endpoint) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT f FROM Feature f WHERE f.endpoint = :endpoint", Feature.class)
                    .setParameter("endpoint", endpoint)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Integer getFeatureIdByEndpoint(String endpoint) {
        Feature feature = findByEndpoint(endpoint);
        return feature != null ? feature.getFeatureId() : null;
    }
} 