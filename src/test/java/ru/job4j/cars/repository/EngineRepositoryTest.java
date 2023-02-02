package ru.job4j.cars.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import ru.job4j.cars.model.Engine;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class EngineRepositoryTest {
    private static final StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder().configure().build();
    private static final SessionFactory SF = new MetadataSources(REGISTRY).buildMetadata().buildSessionFactory();
    private static final CrudRepository CRUD_REPOSITORY = new CrudRepository(SF);

    @After
    public void clearDB() {
        Session session = SF.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createQuery("delete from Engine").executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    @AfterClass
    public static void destroy() {
        StandardServiceRegistryBuilder.destroy(REGISTRY);
    }


    @Test
    public void whenAddAndThenFind() {
        EngineRepository repository = new EngineRepository(CRUD_REPOSITORY);
        Engine engine = new Engine();
        engine.setName("Engine1");
        repository.save(engine);
        Optional<Engine> engineFromDb = repository.findById(engine.getId());
        assertTrue(engineFromDb.isPresent());
        assertThat(engineFromDb.get().getName(), is("Engine1"));

    }
}
