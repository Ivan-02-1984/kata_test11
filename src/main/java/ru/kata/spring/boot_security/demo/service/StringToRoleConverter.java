package ru.kata.spring.boot_security.demo.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    private final RoleService roleService;

    public StringToRoleConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Role convert(String source) {
        Long id = Long.parseLong(source); // Преобразуем строку в Long
        return roleService.findById(id); // Находим роль по ID
    }
}