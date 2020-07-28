package com.aram.demo.utils;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HibernateUtils {
    public static <T> List<T> getEntityListWithRestrictions(Class<T> entityClass, List<SimpleExpression> restrictions) {
        Session session = openSession(entityClass);
        Criteria criteria = session.createCriteria(entityClass);

        for (SimpleExpression simpleExpression: restrictions) {
            criteria.add(simpleExpression);
        }

        return new ArrayList<T>(criteria.list());
    }

    public static Object getEntityByProperty(Class entityClass, String propertyName, Object property) {
        Session session = openSession(entityClass);
        Criteria criteria = session.createCriteria(entityClass);
        criteria.add(Restrictions.eq(propertyName, property));
        return criteria.uniqueResult();
    }

    public static Object createEntity(Object entity) {
        Session session = openSession(entity);

        Transaction transaction = session.beginTransaction();
        Object result = session.get(entity.getClass(), session.save(entity));
        transaction.commit();
        return result;
    }

    public static void deleteEntity(Object entity) {
        Session session = openSession(entity);

        Transaction transaction = session.beginTransaction();
        session.delete(entity);
        transaction.commit();
    }

    public static Session openSession(Object entity) {
        Configuration configuration = new Configuration().configure().addAnnotatedClass(entity.getClass());
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory.openSession();
    }
}
