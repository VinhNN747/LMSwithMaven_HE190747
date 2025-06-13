package com.dao;

import com.util.HibernateUtil;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public abstract class BaseDao<T> {

    protected EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myPU");

    public abstract List<T> list();

    public abstract void create(T entity);

    public abstract void edit(T entity);

    public abstract void delete(T entity);
}
