package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     *
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     *
     * @param user пользователь.
     */
    public void update(User user) {
        try (Session session = sf.openSession()) {
            try {
                session.getTransaction().begin();
                session.createQuery("update User as u SET u.login = :fLogin, u.password =:fPassword where u.id = :fId")
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
    }

    /**
     * Удалить пользователя по id.
     *
     * @param userId ID
     */
    public void delete(int userId) {
        try (Session session = sf.openSession()) {
            try {
                session.getTransaction().begin();
                session.createQuery("delete User where id = :fId")
                        .setParameter("fId", userId)
                        .executeUpdate();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    /**
     * Список пользователь отсортированных по id.
     *
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        List<User> users;
        try (Session session = sf.openSession()) {
            try {
                session.getTransaction().begin();
                users = session.createQuery("from User as u order by u.id ").list();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
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
        try (Session session = sf.openSession()) {
            try {
                session.getTransaction().begin();
                user = session.createQuery("from User as u where u.id = :fId")
                        .setParameter("fId", id)
                        .uniqueResultOptional();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
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
        try (
                Session session = sf.openSession()) {
            try {
                session.getTransaction().begin();
                users = session.createQuery("from User as u where u.login LIKE :fKey")
                        .setParameter("fKey", "%" + key + "%")
                        .list();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
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
        try (Session session = sf.openSession()) {
            try {
                session.getTransaction().begin();
                user = session.createQuery("from User as u where u.login = :fLogin")
                        .setParameter("fLogin", login)
                        .uniqueResultOptional();
                session.getTransaction().commit();
            } catch (final Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
        return user;
    }
}
