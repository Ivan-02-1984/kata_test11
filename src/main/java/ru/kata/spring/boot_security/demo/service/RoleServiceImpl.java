package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional
    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Transactional
    @Override
    public Role findByName(String name) {
        return roleDao.findByName(name);
    }

    @Transactional
    @Override
    public Role findById(Long id) {
        return roleDao.findById(id);
    }

    @Transactional
    @Override
    public void saveRole(Role role){
        roleDao.saveRole(role);
    }

    @Transactional
    @Override
    public void deleteRoleById(Long id) {
        roleDao.deleteRole(id);
    }

    @Transactional
    @Override
    public Set<Role> getRolesByIds(Set<Long> roleIds) {
        return roleIds.stream()
                .map(this::findById)
                .collect(Collectors.toSet());
    }
}