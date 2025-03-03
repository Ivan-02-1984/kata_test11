package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserService {
    void add(User user);
    List<User> allUsers();
    public User findById(Long id);
    void updateUser(User user);
    void deleteById(Long id);
    public User findByUsername(String username);
}
