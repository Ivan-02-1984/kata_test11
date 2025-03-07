package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();
    Role findByName(String name);
    Role findById(Long id);
    void saveRole(Role role);
    void deleteRoleById(Long id);
    Set<Role> getRolesByIds(Set<Long> roleIds);
}
