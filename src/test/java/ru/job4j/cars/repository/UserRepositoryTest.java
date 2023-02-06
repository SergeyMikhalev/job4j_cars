package ru.job4j.cars.repository;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static ru.job4j.cars.repository.HbmRepositoryInitializer.*;

public class UserRepositoryTest {

    @After
    public void clearDB() {
        clear();
    }

    @BeforeClass
    public static void init() {
        setEntityName("User");
    }

    @AfterClass
    public static void destroyAll() {
        destroy();
    }

    @Test
    public void whenSaveAndFindById() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user = new User();
        user.setLogin("user1");
        user.setPassword("pass");
        user = repository.create(user);
        Optional<User> userFromDb = repository.findById(user.getId());

        assertTrue(userFromDb.isPresent());
        assertThat(userFromDb.get().getLogin(), is("user1"));
    }
}