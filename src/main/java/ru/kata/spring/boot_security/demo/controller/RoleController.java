package ru.kata.spring.boot_security.demo.controller;

import ru.kata.spring.boot_security.demo.models.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.service.RoleService;

@Controller
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/addRole")
    public String showAddRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "addRole";
    }

    @PostMapping("/addRole")
    public String addRole(Role role) {
        roleService.addRole(role);
        return "redirect:/login";
    }
}
