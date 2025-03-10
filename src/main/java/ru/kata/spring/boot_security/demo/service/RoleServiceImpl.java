package ru.kata.spring.boot_security.demo.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.exception_handling.RoleNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositiry.RoleRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Role already exists: " + role.getName());
        }
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role updateRole(Long id, Role updatedRole) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));

        if (!existingRole.getName().equals(updatedRole.getName())
                && roleRepository.existsByName(updatedRole.getName())) {
            throw new IllegalArgumentException("Role name already exists: " + updatedRole.getName());
        }

        existingRole.setName(updatedRole.getName());
        return roleRepository.save(existingRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public Set<Role> findRolesByIds(Set<Long> roleIds) {
        return new HashSet<>(roleRepository.findAllById(roleIds));
    }
}