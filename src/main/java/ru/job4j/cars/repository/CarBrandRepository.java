package ru.job4j.cars.repository;

import ru.job4j.cars.model.CarBrand;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CarBrandRepository {

    public static final String FIND_BY_ID = "from CarBrand b where b.id=:fId";
    public static final String FIND_ALL = "from CarBrand";
    public static final String DELETE_QUERY = "delete CarBrand b where b.id =:fId";
    public static final String UPDATE_QUERY = "UPDATE CarBrand as b set b.name=:fName WHERE b.id=:fId";

    private final CrudRepository crudRepository;

    public CarBrandRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Optional<CarBrand> findById(int id) {
        return crudRepository.optional(
                FIND_BY_ID,
                CarBrand.class,
                Map.of("fId", id)
        );
    }

    public List<CarBrand> findAll() {
        return crudRepository.query(FIND_ALL, CarBrand.class);
    }

    public CarBrand save(CarBrand carBrand) {
        crudRepository.run(session -> session.persist(carBrand));
        return carBrand;
    }

    public boolean update(CarBrand carBrand) {
        int updatedCnt = crudRepository.executeUpdate(UPDATE_QUERY,
                Map.of("fName", carBrand.getName(),
                        "fId", carBrand.getId())
        );
        return updatedCnt > 0;
    }

    public boolean delete(int id) {
        int deletedCnt = crudRepository.executeUpdate(DELETE_QUERY,
                Map.of("fId", id));
        return deletedCnt > 0;
    }
}
