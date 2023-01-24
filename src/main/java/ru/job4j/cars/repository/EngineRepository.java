package ru.job4j.cars.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EngineRepository {
    public static final String FIND_BY_ID = "from Engine e where e.id=:fId";
    public static final String FIND_ALL = "from Engine";
    public static final String DELETE_QUERY = "delete Engine e where e.id =:fId";

    private final CrudRepository crudRepository;

    public EngineRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Optional<Engine> findById(int id) {
        return crudRepository.optional(
                FIND_BY_ID,
                Engine.class,
                Map.of("fId", id)
        );
    }

    public List<Engine> findAll(int id) {
        return crudRepository.query(FIND_ALL, Engine.class);
    }

    public Engine save(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    public void update(Engine engine) {
        crudRepository.run(session -> session.update(engine));
    }

    public void delete(int id) {
        crudRepository.run(DELETE_QUERY,
                Map.of("fId", id));
    }
}

