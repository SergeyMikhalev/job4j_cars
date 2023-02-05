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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
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

    @Test
    public void whenFindByIdNonExisting() {
        EngineRepository repository = new EngineRepository(CRUD_REPOSITORY);
        Engine engine = new Engine();
        engine.setName("Engine1");
        repository.save(engine);
        Optional<Engine> engineFromDb = repository.findById(engine.getId() + 1);
        assertFalse(engineFromDb.isPresent());
    }

    @Test
    public void whenUpdateExisting() {
        EngineRepository repository = new EngineRepository(CRUD_REPOSITORY);
        Engine engine = new Engine();
        engine.setName("Engine1");
        repository.save(engine);
        Optional<Engine> engineFromDb = repository.findById(engine.getId());
        assertTrue(engineFromDb.isPresent());
        assertThat(engineFromDb.get().getName(), is("Engine1"));

        engine.setName("Updated");
        boolean updated = repository.update(engine);
        engineFromDb = repository.findById(engine.getId());
        assertTrue(engineFromDb.isPresent());
        assertThat(engineFromDb.get().getName(), is("Updated"));
        assertTrue(updated);
    }

    @Test
    public void whenUpdateNonExisting() {
        EngineRepository repository = new EngineRepository(CRUD_REPOSITORY);
        Engine engine = new Engine();
        engine.setName("Engine1");
        repository.save(engine);
        Optional<Engine> engineFromDb = repository.findById(engine.getId());
        assertTrue(engineFromDb.isPresent());
        assertThat(engineFromDb.get().getName(), is("Engine1"));
        engine.setName("Updated");
        engine.setId(engine.getId() + 1);
        boolean updated = repository.update(engine);
        assertFalse(updated);
    }

    @Test
    public void whenFindAllOnEmpty() {
        EngineRepository repository = new EngineRepository(CRUD_REPOSITORY);
        List<Engine> engines = repository.findAll();
        assertThat(engines.size(), is(0));
    }

    @Test
    public void whenFindAllWithResult() {
        EngineRepository repository = new EngineRepository(CRUD_REPOSITORY);
        Engine engine1 = new Engine();
        engine1.setName("Engine1");
        repository.save(engine1);
        Engine engine2 = new Engine();
        engine2.setName("Engine2");
        repository.save(engine2);
        List<Engine> expectingEngines = List.of(engine1, engine2);
        List<Engine> enginesFromDb = repository.findAll();
        boolean containsTheSame = enginesFromDb.containsAll(expectingEngines);
        assertThat(enginesFromDb.size(), is(2));
        assertTrue(containsTheSame);
    }

    @Test
    public void whenDeleteExisting() {
        EngineRepository repository = new EngineRepository(CRUD_REPOSITORY);
        Engine engine = new Engine();
        engine.setName("Engine1");
        repository.save(engine);
        Optional<Engine> engineFromDb = repository.findById(engine.getId());
        assertTrue(engineFromDb.isPresent());
        assertThat(engineFromDb.get().getName(), is("Engine1"));
        boolean deleteResult = repository.delete(engine.getId());
        engineFromDb = repository.findById(engine.getId());
        assertFalse(engineFromDb.isPresent());
        assertTrue(deleteResult);
    }

    @Test
    public void whenDeleteNotExisting() {
        EngineRepository repository = new EngineRepository(CRUD_REPOSITORY);
        Engine engine = new Engine();
        engine.setName("Engine1");
        repository.save(engine);
        boolean deleteResult = repository.delete(engine.getId() + 1);
        assertFalse(deleteResult);
    }
}
