package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRepositoryInitializer {
    private static final StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder().configure().build();
    private static final SessionFactory SF = new MetadataSources(REGISTRY).buildMetadata().buildSessionFactory();
    private static final CrudRepository CRUD_REPOSITORY = new CrudRepository(SF);
    private static final String CLEAR_DB = "delete from ";
    private static String entityName;


    public static void clear() {
        /*CRUD_REPOSITORY.run(session->session.createQuery(CLEAR_DB + entityName).executeUpdate());*/
        Session session = SF.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createQuery(CLEAR_DB + entityName).executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static void setEntityName(String newEntityName) {
        entityName = newEntityName;
    }

    public static void destroy() {
        StandardServiceRegistryBuilder.destroy(REGISTRY);
    }

    public static CrudRepository getCrudRepository() {
        return CRUD_REPOSITORY;
    }
}
