package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role findByName(String name);
    Role findById(Long id);
    void saveRole(Role role);
    Role addRole(Role role);
}
