package ru.job4j.cars.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Driver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DriverRepository {
    public static final String FIND_BY_ID = "from Driver d where d.id=:fId";
    public static final String FIND_ALL = "from Driver";
    public static final String DELETE_QUERY = "delete Driver d where d.id =:fId";
    public static final String UPDATE_QUERY = "UPDATE Driver d SET d.name=:fName where d.id=:fId";

    private final CrudRepository crudRepository;

    public DriverRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Optional<Driver> findById(int id) {
        return crudRepository.optional(
                FIND_BY_ID,
                Driver.class,
                Map.of("fId", id)
        );
    }

    public List<Driver> findAll() {
        return crudRepository.query(FIND_ALL, Driver.class);
    }

    public Driver save(Driver driver) {
        crudRepository.run(session -> session.persist(driver));
        return driver;
    }

    public boolean update(Driver driver) {
        int updateCnt = crudRepository.executeUpdate(UPDATE_QUERY,
                Map.of("fName", driver.getName(),
                        "fId", driver.getId())
        );
        return updateCnt > 0;
    }

    public boolean delete(int id) {
        int deleteCnt = crudRepository.executeUpdate(DELETE_QUERY,
                Map.of("fId", id));
        return deleteCnt > 0;
    }
}


