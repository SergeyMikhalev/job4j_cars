package ru.job4j.cars.repository;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.job4j.cars.repository.HbmRepositoryInitializer.getCrudRepository;
import static ru.job4j.cars.repository.HbmRepositoryInitializer.setEntityName;

public class EngineRepositoryTest {

    @After
    public void clearDB() {
        HbmRepositoryInitializer.clear();
    }

    @BeforeClass
    public static void init() {
        setEntityName("Engine");
    }

    @AfterClass
    public static void destroy() {
        HbmRepositoryInitializer.destroy();
    }


    @Test
    public void whenAddAndThenFind() {
        EngineRepository repository = new EngineRepository(getCrudRepository());
        Engine engine = new Engine();
        engine.setName("Engine1");
        repository.save(engine);
        Optional<Engine> engineFromDb = repository.findById(engine.getId());
        assertTrue(engineFromDb.isPresent());
        assertThat(engineFromDb.get().getName(), is("Engine1"));
    }

    @Test
    public void whenFindByIdNonExisting() {
        EngineRepository repository = new EngineRepository(getCrudRepository());
        Engine engine = new Engine();
        engine.setName("Engine1");
        repository.save(engine);
        Optional<Engine> engineFromDb = repository.findById(engine.getId() + 1);
        assertFalse(engineFromDb.isPresent());
    }

    @Test
    public void whenUpdateExisting() {
        EngineRepository repository = new EngineRepository(getCrudRepository());
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
        EngineRepository repository = new EngineRepository(getCrudRepository());
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
        EngineRepository repository = new EngineRepository(getCrudRepository());
        List<Engine> engines = repository.findAll();
        assertThat(engines.size(), is(0));
    }

    @Test
    public void whenFindAllWithResult() {
        EngineRepository repository = new EngineRepository(getCrudRepository());
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
        EngineRepository repository = new EngineRepository(getCrudRepository());
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
        EngineRepository repository = new EngineRepository(getCrudRepository());
        Engine engine = new Engine();
        engine.setName("Engine1");
        repository.save(engine);
        boolean deleteResult = repository.delete(engine.getId() + 1);
        assertFalse(deleteResult);
    }
}
