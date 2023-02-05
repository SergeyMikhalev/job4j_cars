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
import ru.job4j.cars.model.Post;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PostRepositoryTest {
    private static final StandardServiceRegistry REGISTRY = new StandardServiceRegistryBuilder().configure().build();
    private static final SessionFactory SF = new MetadataSources(REGISTRY).buildMetadata().buildSessionFactory();
    private static final CrudRepository CRUD_REPOSITORY = new CrudRepository(SF);
    public static final String CLEAN_DB = "delete from Post";

    @After
    public void clearDB() {
        Session session = SF.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.createQuery(CLEAN_DB).executeUpdate();
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
    public void whenTodayPostsOnEmpty() {
        PostRepository repository = new PostRepository(CRUD_REPOSITORY);
        List<Post> posts = repository.todayPosts();
        assertThat(posts.size(), is(0));
    }


}
