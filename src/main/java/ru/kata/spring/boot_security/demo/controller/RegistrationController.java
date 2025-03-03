package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.models.Role;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class RegistrationController {
    private final UserService userService;
    private final RoleService roleService;

    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        List<Role> allRoles = roleService.getAllRoles(); // Получаем все роли

        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.add(user);
        return "redirect:/login";
    }
}
