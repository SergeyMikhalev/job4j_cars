package ru.job4j.cars.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepository {
    public static final String POSTS_AFTER_DATE_TIME = "from Post p where p.created >= :fDateTime";
    public static final String POSTS_WHERE_PHOTO_EXISTS = "form Post p where p.photo is not null";
    public static final String FIND_BY_CAR_BRAND = "from Post p where p.car.brand=:fBrand";

    private final CrudRepository crudRepository;

    public PostRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public List<Post> todayPosts() {
        LocalDateTime todayEdge = LocalDate.now().atTime(0, 0);
        return crudRepository.query(
                POSTS_AFTER_DATE_TIME,
                Post.class,
                Map.of("fDateTime", todayEdge));
    }

    public List<Post> findWherePhotoExists() {
        return crudRepository.query(POSTS_WHERE_PHOTO_EXISTS, Post.class);
    }

    public List<Post> findByCarBrand(String carBrand) {
        return crudRepository.query(
                FIND_BY_CAR_BRAND,
                Post.class,
                Map.of("fBrand", carBrand));
    }


}
