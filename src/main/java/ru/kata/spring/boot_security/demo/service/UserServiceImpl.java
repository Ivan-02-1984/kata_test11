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
import ru.kata.spring.boot_security.demo.models.UserUpdateForm;
import ru.kata.spring.boot_security.demo.repositiry.RoleRepository;
import ru.kata.spring.boot_security.demo.repositiry.UserRepository;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService, @Qualifier("myPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
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
    public void updateUser(UserUpdateForm userUpdateForm) {
        User existingUser = userRepository.findById(userUpdateForm.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Обновляем пароль только если новое значение предоставлено
        if (userUpdateForm.getPassword() != null && !userUpdateForm.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateForm.getPassword()));
        }

        // Обновляем остальные поля
        existingUser.setUsername(userUpdateForm.getUsername());
        existingUser.setFirstName(userUpdateForm.getFirstName());
        existingUser.setLastName(userUpdateForm.getLastName());
        existingUser.setAge(userUpdateForm.getAge());
        existingUser.setEmail(userUpdateForm.getEmail());
        existingUser.setEnabled(userUpdateForm.isEnabled());

        // Обновляем роли, если они предоставлены
        if (userUpdateForm.getRoleIds() != null && !userUpdateForm.getRoleIds().isEmpty()) {
            Set<Role> roles = roleService.findRolesByIds(userUpdateForm.getRoleIds());
            existingUser.setRoles(roles);
        }

        userRepository.save(existingUser);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
@Transactional
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllWithRoles();// Инициализация внутри транзакции
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
        return userRepository.findByUsernameWithRoles(username)
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