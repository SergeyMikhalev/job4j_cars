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
import ru.job4j.cars.model.Driver;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DriverRepositoryTest {
    private static final StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder().configure().build();
    private static final SessionFactory SF = new MetadataSources(REGISTRY).buildMetadata().buildSessionFactory();
    private static final CrudRepository CRUD_REPOSITORY = new CrudRepository(SF);
    public static final String CLEAR_DB = "delete from Driver";

    @After
    public void clearDB() {
        Session session = SF.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createQuery(CLEAR_DB).executeUpdate();
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
        DriverRepository repository = new DriverRepository(CRUD_REPOSITORY);
        Driver driver = new Driver();
        driver.setName("Driver1");
        repository.save(driver);
        Optional<Driver> driverFromDb = repository.findById(driver.getId());
        assertTrue(driverFromDb.isPresent());
        assertThat(driverFromDb.get().getName(), is("Driver1"));
    }

    @Test
    public void whenFindByIdNonExisting() {
        DriverRepository repository = new DriverRepository(CRUD_REPOSITORY);
        Driver driver = new Driver();
        driver.setName("Engine1");
        repository.save(driver);
        Optional<Driver> driverFromDb = repository.findById(driver.getId() + 1);
        assertFalse(driverFromDb.isPresent());
    }

    @Test
    public void whenUpdateExisting() {
        DriverRepository repository = new DriverRepository(CRUD_REPOSITORY);
        Driver driver = new Driver();
        driver.setName("Driver1");
        repository.save(driver);
        Optional<Driver> driverFromDb = repository.findById(driver.getId());
        assertTrue(driverFromDb.isPresent());
        assertThat(driverFromDb.get().getName(), is("Driver1"));

        driver.setName("Updated");
        boolean updated = repository.update(driver);
        driverFromDb = repository.findById(driver.getId());
        assertTrue(driverFromDb.isPresent());
        assertThat(driverFromDb.get().getName(), is("Updated"));
        assertTrue(updated);
    }

    @Test
    public void whenUpdateNonExisting() {
        DriverRepository repository = new DriverRepository(CRUD_REPOSITORY);
        Driver driver = new Driver();
        driver.setName("Driver1");
        repository.save(driver);
        Optional<Driver> driverFromDb = repository.findById(driver.getId());
        assertTrue(driverFromDb.isPresent());
        assertThat(driverFromDb.get().getName(), is("Driver1"));
        driver.setName("Updated");
        driver.setId(driver.getId() + 1);
        boolean updated = repository.update(driver);
        assertFalse(updated);
    }

    @Test
    public void whenFindAllOnEmpty() {
        DriverRepository repository = new DriverRepository(CRUD_REPOSITORY);
        List<Driver> engines = repository.findAll();
        assertThat(engines.size(), is(0));
    }

    @Test
    public void whenFindAllWithResult() {
        DriverRepository repository = new DriverRepository(CRUD_REPOSITORY);
        Driver driver1 = new Driver();
        driver1.setName("Driver1");
        repository.save(driver1);
        Driver driver2 = new Driver();
        driver2.setName("Driver2");
        repository.save(driver2);
        List<Driver> expectingDrivers = List.of(driver1, driver2);
        List<Driver> driversFromDb = repository.findAll();
        boolean containsTheSame = driversFromDb.containsAll(expectingDrivers);
        assertThat(driversFromDb.size(), is(2));
        assertTrue(containsTheSame);
    }

    @Test
    public void whenDeleteExisting() {
        DriverRepository repository = new DriverRepository(CRUD_REPOSITORY);
        Driver driver = new Driver();
        driver.setName("Driver1");
        repository.save(driver);
        Optional<Driver> driverFromDb = repository.findById(driver.getId());
        assertTrue(driverFromDb.isPresent());
        assertThat(driverFromDb.get().getName(), is("Driver1"));
        boolean deleteResult = repository.delete(driver.getId());
        driverFromDb = repository.findById(driver.getId());
        assertFalse(driverFromDb.isPresent());
        assertTrue(deleteResult);
    }

    @Test
    public void whenDeleteNotExisting() {
        DriverRepository repository = new DriverRepository(CRUD_REPOSITORY);
        Driver driver = new Driver();
        driver.setName("Driver1");
        repository.save(driver);
        boolean deleteResult = repository.delete(driver.getId() + 1);
        assertFalse(deleteResult);
    }
}
