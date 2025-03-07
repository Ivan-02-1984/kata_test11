package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserDao {
   void add(User user);
   public List<User> allUsers();
   public User findById(Long id);
   void updateUser(User user);
   void deleteById(Long id);
   public User findByUsername(String username);
}
