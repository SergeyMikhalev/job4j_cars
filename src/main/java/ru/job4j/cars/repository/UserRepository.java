package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    public static final String DELETE_USER_QUERY = "delete User where id = :fId";
    public static final String ALL_USERS_ORDER_BY_ID_QUERY = "from User as u order by u.id ";
    public static final String FIND_USER_BY_ID_QUERY = "from User as u where u.id = :fId";
    public static final String FIND_USERS_LOGIN_LIKE_QUERY = "from User as u where u.login LIKE :fKey";
    public static final String FIND_USER_BY_LOGIN_QUERY = "from User as u where u.login = :fLogin";

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     *
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
            crudRepository.run(session -> session.persist(user));
            return user;
    }

    /**
     * Обновить в базе пользователя.
     *
     * @param user пользователь.
     */
    public void update(User user) {
        crudRepository.run(session -> session.update(user));
    }

    /**
     * Удалить пользователя по id.
     *
     * @param userId ID
     */
    public void delete(int userId) {
        crudRepository.run(
                DELETE_USER_QUERY,
                Map.of("fId", userId)
        );
    }

    /**
     * Список пользователь отсортированных по id.
     *
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        return crudRepository.query(ALL_USERS_ORDER_BY_ID_QUERY, User.class);
    }

    /**
     * Найти пользователя по ID
     *
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        return crudRepository.optional(
                FIND_USER_BY_ID_QUERY, User.class,
                Map.of("fId", id)
        );
    }

    /**
     * Список пользователей по login LIKE %key%
     *
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                FIND_USERS_LOGIN_LIKE_QUERY, User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    /**
     * Найти пользователя по login.
     *
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                FIND_USER_BY_LOGIN_QUERY, User.class,
                Map.of("fLogin", login)
        );
    }

}
