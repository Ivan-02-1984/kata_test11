package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    private final EntityManager entityManager;

    public UserDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    public List<User> allUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteById(Long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    @Override
    public User findByUsername(String username) {
        return entityManager.createQuery(
                        "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username",
                        User.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
