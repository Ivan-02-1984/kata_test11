package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleDao {
    List<Role> getAllRoles();
    Role findByName(String name);
    Role findById(Long id);
    void saveRole(Role role);
}
