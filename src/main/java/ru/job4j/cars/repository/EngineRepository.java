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
    public static final String UPDATE_QUERY = "UPDATE Engine as e set e.name=:fName WHERE e.id=:fId";

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

    public List<Engine> findAll() {
        return crudRepository.query(FIND_ALL, Engine.class);
    }

    public Engine save(Engine engine) {
        crudRepository.run(session -> session.persist(engine));
        return engine;
    }

    public boolean update(Engine engine) {
        int updatedCnt = crudRepository.executeUpdate(UPDATE_QUERY,
                Map.of("fName", engine.getName(),
                        "fId", engine.getId())
        );
        return updatedCnt > 0;
    }

    public boolean delete(int id) {
        int deletedCnt = crudRepository.executeUpdate(DELETE_QUERY,
                Map.of("fId", id));
        return deletedCnt > 0;
    }
}

