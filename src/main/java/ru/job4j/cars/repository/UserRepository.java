package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    public static final String UPDATE_USER_QUERY =
            "update User as u SET u.login = :fLogin, u.password =:fPassword where u.id = :fId";
    public static final String DELETE_USER_QUERY = "delete User where id = :fId";
    public static final String ALL_USERS_ORDER_BY_ID_QUERY = "from User as u order by u.id ";
    public static final String FIND_USER_BY_ID_QUERY = "from User as u where u.id = :fId";
    public static final String FIND_USERS_LOGIN_LIKE_QUERY = "from User as u where u.login LIKE :fKey";
    public static final String FIND_USER_BY_LOGIN_QUERY = "from User as u where u.login = :fLogin";
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     *
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     *
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try (session) {
            session.getTransaction().begin();
            session.createQuery(UPDATE_USER_QUERY)
                    .setParameter("fId", user.getId())
                    .setParameter("fLogin", user.getLogin())
                    .setParameter("fPassword", user.getPassword())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Удалить пользователя по id.
     *
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.getTransaction().begin();
            session.createQuery(DELETE_USER_QUERY)
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Список пользователь отсортированных по id.
     *
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        List<User> users;
        Session session = sf.openSession();
        try {
            session.getTransaction().begin();
            users = session.createQuery(ALL_USERS_ORDER_BY_ID_QUERY).list();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return users;
    }

    /**
     * Найти пользователя по ID
     *
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        Optional<User> user;
        Session session = sf.openSession();
        try {
            session.getTransaction().begin();
            user = session.createQuery(FIND_USER_BY_ID_QUERY)
                    .setParameter("fId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return user;
    }

    /**
     * Список пользователей по login LIKE %key%
     *
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        List<User> users;
        Session session = sf.openSession();
        try (session) {
            session.getTransaction().begin();
            users = session.createQuery(FIND_USERS_LOGIN_LIKE_QUERY)
                    .setParameter("fKey", "%" + key + "%")
                    .list();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return users;
    }

    /**
     * Найти пользователя по login.
     *
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Optional<User> user;
        Session session = sf.openSession();
        try {
            session.getTransaction().begin();
            user = session.createQuery(FIND_USER_BY_LOGIN_QUERY)
                    .setParameter("fLogin", login)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
        return user;
    }

}
