package ru.kata.spring.boot_security.demo.service;

import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.exception_handling.DuplicateUsernameException;
import ru.kata.spring.boot_security.demo.exception_handling.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositiry.RoleRepository;
import ru.kata.spring.boot_security.demo.repositiry.UserRepository;
import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,@Qualifier("myPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateUsernameException("Username already exists: " + user.getUsername());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            setManagedRoles(user);
        } catch (RoleNotFoundException e) {
            throw new RuntimeException("Role not found", e);
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setEnabled(updatedUser.isEnabled());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.getRoles() != null) {
            try {
                setManagedRoles(updatedUser);
            } catch (RoleNotFoundException e) {
                throw new RuntimeException("Role not found", e);
            }
            existingUser.setRoles(updatedUser.getRoles());
        }

        userRepository.save(existingUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> Hibernate.initialize(user.getRoles())); // Инициализация внутри транзакции
        return users;
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    private void setManagedRoles(User user) throws RoleNotFoundException {
        List<Long> roleIds = user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toList());

        List<Role> managedRoles = roleRepository.findAllById(roleIds);

        if (managedRoles.size() != roleIds.size()) {
            throw new RoleNotFoundException("One or more roles not found");
        }

        user.setRoles(new HashSet<>(managedRoles));
    }
}