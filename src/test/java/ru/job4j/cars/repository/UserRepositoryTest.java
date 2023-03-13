package ru.job4j.cars.repository;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
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

    @Test
    public void whenSaveAndFindByWrongId() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user = new User();
        user.setLogin("user1");
        user.setPassword("pass");
        user = repository.create(user);
        Optional<User> userFromDb = repository.findById(user.getId() + 1);
        assertFalse(userFromDb.isPresent());
    }

    @Test
    public void whenUpdate() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user = new User();
        user.setLogin("user1");
        user.setPassword("pass");
        user = repository.create(user);
        user.setPassword("newPass");
        repository.update(user);
        Optional<User> userFromDb = repository.findById(user.getId());
        assertTrue(userFromDb.isPresent());
        assertThat(userFromDb.get().getLogin(), is("user1"));
        assertThat(userFromDb.get().getPassword(), is("newPass"));
    }

    @Test(expected = Exception.class)
    public void whenUpdateNonExisting() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user = new User();
        user.setLogin("user1");
        user.setPassword("pass");
        user = repository.create(user);
        user.setPassword("newPass");
        user.setId(user.getId() + 1);
        repository.update(user);
    }

    @Test
    public void whenDelete() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user = new User();
        user.setLogin("user1");
        user.setPassword("pass");
        user = repository.create(user);
        Optional<User> userFromDb = repository.findById(user.getId());
        assertTrue(userFromDb.isPresent());
        assertThat(userFromDb.get().getLogin(), is("user1"));
        repository.delete(user.getId());
        userFromDb = repository.findById(user.getId());
        assertFalse(userFromDb.isPresent());
    }

    @Test
    public void whenDeleteNonExisting() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user = new User();
        user.setLogin("user1");
        user.setPassword("pass");
        user = repository.create(user);
        Optional<User> userFromDb = repository.findById(user.getId());
        assertTrue(userFromDb.isPresent());
        assertThat(userFromDb.get().getLogin(), is("user1"));
        repository.delete(user.getId() + 1);
        userFromDb = repository.findById(user.getId());
        assertTrue(userFromDb.isPresent());
        assertThat(userFromDb.get().getLogin(), is("user1"));
    }

    @Test
    public void whenSaveAndFindByLogin() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user1 = new User();
        user1.setLogin("user1");
        user1.setPassword("pass1");
        User user2 = new User();
        user2.setLogin("user2");
        user2.setPassword("pass2");
        repository.create(user1);
        repository.create(user2);
        Optional<User> userFromDb = repository.findByLogin("user1");
        assertTrue(userFromDb.isPresent());
        assertThat(userFromDb.get().getPassword(), is("pass1"));
    }

    @Test
    public void whenSaveAndFindByWrongLogin() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user1 = new User();
        user1.setLogin("user1");
        user1.setPassword("pass1");
        User user2 = new User();
        user2.setLogin("user2");
        user2.setPassword("pass2");
        repository.create(user1);
        repository.create(user2);
        Optional<User> userFromDb = repository.findByLogin("Petr");
        assertFalse(userFromDb.isPresent());
    }

    @Test
    public void whenSaveAndFindByLikeLogin() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user1 = new User();
        user1.setLogin("Sveta");
        user1.setPassword("pass1");
        User user2 = new User();
        user2.setLogin("Ira");
        user2.setPassword("pass2");
        User user3 = new User();
        user3.setLogin("Dora");
        user3.setPassword("pass2");
        repository.create(user1);
        repository.create(user2);
        repository.create(user3);
        List<User> usersFromDb = repository.findByLikeLogin("ra");
        assertThat(usersFromDb.size(), is(2));
        List<String> logins = usersFromDb.stream().map(user -> user.getLogin()).collect(Collectors.toList());
        assertTrue(logins.containsAll(List.of("Ira", "Dora")));
    }

    @Test
    public void whenSaveAndFindByLikeLoginWrong() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user1 = new User();
        user1.setLogin("Sveta");
        user1.setPassword("pass1");
        User user2 = new User();
        user2.setLogin("Ira");
        user2.setPassword("pass2");
        User user3 = new User();
        user3.setLogin("Dora");
        user3.setPassword("pass2");
        repository.create(user1);
        repository.create(user2);
        repository.create(user3);
        List<User> usersFromDb = repository.findByLikeLogin("za");
        assertThat(usersFromDb.size(), is(0));
    }

    @Test
    public void whenFindAllOrderById() {
        UserRepository repository = new UserRepository(getCrudRepository());
        User user1 = new User();
        user1.setLogin("Sveta");
        user1.setPassword("pass1");
        User user2 = new User();
        user2.setLogin("Ira");
        user2.setPassword("pass2");
        User user3 = new User();
        user3.setLogin("Dora");
        user3.setPassword("pass2");
        repository.create(user1);
        repository.create(user2);
        repository.create(user3);
        List<User> usersFromDb = repository.findAllOrderById();
        assertThat(usersFromDb.size(), is(3));
        assertTrue(usersFromDb.get(0).getId() < usersFromDb.get(1).getId());
        assertTrue(usersFromDb.get(1).getId() < usersFromDb.get(2).getId());
    }

    @Test
    public void whenFindAllOrderByIdOnEmpty() {
        UserRepository repository = new UserRepository(getCrudRepository());
        List<User> usersFromDb = repository.findAllOrderById();
        assertThat(usersFromDb.size(), is(0));
    }

}