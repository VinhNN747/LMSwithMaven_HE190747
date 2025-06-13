package com.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public abstract class BaseDao<T> {

    private final EntityManagerFactory entityManagerFactory;

    public BaseDao() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("myPU");
    }

    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public abstract List<T> list();

    public abstract void create(T entity);

    public abstract void edit(T entity);

    public abstract void delete(T entity);
}
