package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    void deleteUser(Long id);
    void updateUser(User updatedUser);
    User getUserById(Long id);
    List<User> getAllUsers();
    User findByUsername(String username);
}
