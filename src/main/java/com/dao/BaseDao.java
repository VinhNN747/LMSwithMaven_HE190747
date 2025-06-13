package com.dao;

import com.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;

public abstract class BaseDao<T> {

    protected SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    protected Session getSession() {
        return sessionFactory.openSession();
    }

    public abstract List<T> list();

    public abstract void create(T entity);

    public abstract void edit(T entity);

    public abstract void delete(T entity);
}
