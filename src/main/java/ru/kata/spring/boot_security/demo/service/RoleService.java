package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.Role;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    List<Role> getAllRoles();
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);
    Set<Role> findRolesByIds(Set<Long> roleIds);
    ;
}
