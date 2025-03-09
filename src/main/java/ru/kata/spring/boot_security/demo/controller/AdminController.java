package ru.kata.spring.boot_security.demo.controller;

import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(username);

        List<User> users = userService.getAllUsers();
        users.forEach(user -> Hibernate.initialize(user.getRoles()));

        model.addAttribute("user", new User()); // Добавляем пустой объект для формы
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("allUser", users);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("allErrors", Collections.emptyList());

        return "showAllUsers";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/editUser")
    public String showEditForm(@RequestParam Long id, Model model) {
        User user = userService.getUserById(id);
        Hibernate.initialize(user.getRoles());
        List<Role> allRoles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);

        return "edit_user";
    }

    @PostMapping("/editUser")
    public String updateUser(
            @ModelAttribute User user,
            @RequestParam(name = "roleIds", required = false) Set<Long> roleIds) { // Изменили здесь

        Set<Role> roles = (roleIds != null && !roleIds.isEmpty())
                ? new HashSet<>(roleService.findRolesByIds(new ArrayList<>(roleIds)))
                : Collections.emptySet();

        user.setRoles(roles);
        userService.updateUser(user);

        return "redirect:/admin";
    }
}
