package ru.job4j.cars.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CarRepository {
    public static final String FIND_BY_ID = "from Car c JOIN FETCH c.owners where c.id=:fId";
    public static final String FIND_ALL = "from Car c JOIN FETCH c.owners";
    public static final String DELETE_QUERY = "delete Car c where c.id =:fId";

    private final CrudRepository crudRepository;

    public CarRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Optional<Car> findById(int id) {
        return crudRepository.optional(
                FIND_BY_ID,
                Car.class,
                Map.of("fId", id)
        );
    }

    public List<Car> findAll(int id) {
        return crudRepository.query(FIND_ALL, Car.class);
    }

    public Car save(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    public void update(Car car) {
        crudRepository.run(session -> session.update(car));
    }

    public void delete(int id) {
        crudRepository.run(DELETE_QUERY,
                Map.of("fId", id));
    }
}
