package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {

    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role existingRole = roleService.findById(role.getId());
            if (existingRole != null) {
                roles.add(existingRole);
            }
        }
        user.setRoles(roles);

        userDao.add(user); // Сохраняем пользователя
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userDao.findById(user.getId());
        user.setUsername(user.getUsername());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            roles.add(roleService.findById(role.getId()));
        }
        user.setRoles(roles);

        userDao.updateUser(user);
    }

    @Transactional
    @Override
    public List<User> allUsers() {
        return userDao.allUsers();
    }

    @Transactional
    @Override
    public User findById(Long id) {
        return userDao.findById(id);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Transactional
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
