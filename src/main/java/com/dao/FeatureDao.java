package com.dao;

import com.entity.Feature;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class FeatureDao extends BaseDao<Feature> {

    public FeatureDao() {
        super();
    }

    public List<Feature> list(String name, Integer pageNumber, Integer pageSize) {
        EntityManager em = getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT f FROM Feature f WHERE 1=1");
            if (name != null && !name.isEmpty()) {
                jpql.append(" AND f.featureName LIKE :name");
            }
            jpql.append(" ORDER BY f.endpoint");
            var query = em.createQuery(jpql.toString(), Feature.class);
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
    public List<Feature> list() {
        return list(null, null, null);
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

    public Feature get(Integer id) {
        return getEntityManager().find(Feature.class, id);
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
}
