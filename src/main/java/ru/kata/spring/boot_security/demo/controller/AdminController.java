package ru.kata.spring.boot_security.demo.controller;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.models.UserUpdateForm;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import org.springframework.validation.BindingResult;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService,@Qualifier("myPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
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
    public String editUser(@ModelAttribute("user") UserUpdateForm userUpdateForm,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/edit-user"; // Вернуть форму с ошибками
        }
        System.out.println("Role IDs: " + userUpdateForm.getRoleIds());

        userService.updateUser(userUpdateForm);
        redirectAttributes.addFlashAttribute("message", "Пользователь успешно обновлен");
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/new";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user")@Validated User user,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allErrors", result.getAllErrors()); // Передаем ошибки валидации
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "admin/new";
        }
        userService.createUser(user);
        return "redirect:/admin";
    }
}
