package ru.job4j.cars.repository;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.CarBrand;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.job4j.cars.repository.HbmRepositoryInitializer.getCrudRepository;
import static ru.job4j.cars.repository.HbmRepositoryInitializer.setEntityName;

public class CarBrandRepositoryTest {
    @After
    public void clearDB() {
        HbmRepositoryInitializer.clear();
    }

    @BeforeClass
    public static void init() {
        setEntityName("CarBrand");
    }

    @AfterClass
    public static void destroy() {
        HbmRepositoryInitializer.destroy();
    }



    @Test
    public void whenAddAndThenFind() {
        CarBrandRepository repository = new CarBrandRepository(getCrudRepository());
        CarBrand brand = new CarBrand();
        brand.setName("Brand1");
        repository.save(brand);
        Optional<CarBrand> brandFromDb = repository.findById(brand.getId());
        assertTrue(brandFromDb.isPresent());
        assertThat(brandFromDb.get().getName(), is("Brand1"));
    }

    @Test
    public void whenFindByIdNonExisting() {
        CarBrandRepository repository = new CarBrandRepository(getCrudRepository());
        CarBrand carBrand = new CarBrand();
        carBrand.setName("Brand1");
        repository.save(carBrand);
        Optional<CarBrand> brandFromDb = repository.findById(carBrand.getId() + 1);
        assertFalse(brandFromDb.isPresent());
    }

    @Test
    public void whenUpdateExisting() {
        CarBrandRepository repository = new CarBrandRepository(getCrudRepository());
        CarBrand carBrand = new CarBrand();
        carBrand.setName("Brand1");
        repository.save(carBrand);
        Optional<CarBrand> brandFromDb = repository.findById(carBrand.getId());
        assertTrue(brandFromDb.isPresent());
        assertThat(brandFromDb.get().getName(), is("Brand1"));

        carBrand.setName("Updated");
        boolean updated = repository.update(carBrand);
        brandFromDb = repository.findById(carBrand.getId());
        assertTrue(brandFromDb.isPresent());
        assertThat(brandFromDb.get().getName(), is("Updated"));
        assertTrue(updated);
    }

    @Test
    public void whenUpdateNonExisting() {
        CarBrandRepository repository = new CarBrandRepository(getCrudRepository());
        CarBrand carBrand = new CarBrand();
        carBrand.setName("Brand1");
        repository.save(carBrand);
        Optional<CarBrand> engineFromDb = repository.findById(carBrand.getId());
        assertTrue(engineFromDb.isPresent());
        assertThat(engineFromDb.get().getName(), is("Brand1"));
        carBrand.setName("Updated");
        carBrand.setId(carBrand.getId() + 1);
        boolean updated = repository.update(carBrand);
        assertFalse(updated);
    }

    @Test
    public void whenFindAllOnEmpty() {
        CarBrandRepository repository = new CarBrandRepository(getCrudRepository());
        List<CarBrand> engines = repository.findAll();
        assertThat(engines.size(), is(0));
    }

    @Test
    public void whenFindAllWithResult() {
        CarBrandRepository repository = new CarBrandRepository(getCrudRepository());
        CarBrand carBrand1 = new CarBrand();
        carBrand1.setName("Brand1");
        repository.save(carBrand1);
        CarBrand carBrand2 = new CarBrand();
        carBrand2.setName("Brand2");
        repository.save(carBrand2);
        List<CarBrand> expectingBrands = List.of(carBrand1, carBrand2);
        List<CarBrand> brandsFromDb = repository.findAll();
        boolean containsTheSame = brandsFromDb.containsAll(expectingBrands);
        assertThat(brandsFromDb.size(), is(2));
        assertTrue(containsTheSame);
    }

    @Test
    public void whenDeleteExisting() {
        CarBrandRepository repository = new CarBrandRepository(getCrudRepository());
        CarBrand carBrand = new CarBrand();
        carBrand.setName("Engine1");
        repository.save(carBrand);
        Optional<CarBrand> brandFromDb = repository.findById(carBrand.getId());
        assertTrue(brandFromDb.isPresent());
        assertThat(brandFromDb.get().getName(), is("Engine1"));
        boolean deleteResult = repository.delete(carBrand.getId());
        brandFromDb = repository.findById(carBrand.getId());
        assertFalse(brandFromDb.isPresent());
        assertTrue(deleteResult);
    }

    @Test
    public void whenDeleteNotExisting() {
        CarBrandRepository repository = new CarBrandRepository(getCrudRepository());
        CarBrand carBrand = new CarBrand();
        carBrand.setName("Engine1");
        repository.save(carBrand);
        boolean deleteResult = repository.delete(carBrand.getId() + 1);
        assertFalse(deleteResult);
    }
}