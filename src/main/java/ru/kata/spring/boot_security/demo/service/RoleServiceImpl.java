package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.models.Role;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleDao roleDao, RoleRepository roleRepository) {
        this.roleDao = roleDao;
        this.roleRepository = roleRepository;
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
    public void saveRole(Role role) {
        roleDao.saveRole(role);
    }

    @Transactional
    @Override
    public Role addRole(Role role){
        return roleRepository.save(role);
    }
}