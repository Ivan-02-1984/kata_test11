package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private  UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public String user(Model model) {
        model.addAttribute("user", new User());
        return "admin_options";
    }

    @GetMapping("/for_admin")
    public String showAllUsers(Model model) {
        List<User> Users = userService.allUsers();
        model.addAttribute("allUser", Users);
        return "showAllUsers";
    }

    @GetMapping("/editUser")
    public String showEditForm(@RequestParam("userID") Long id, Model model) {
        User user = userService.findById(id);
        List<Role> allRoles = roleService.getAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);

        return "edit_user";
    }

    @PostMapping("update")
    public String updateUser(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {
            userService.deleteById(id);
        return "redirect:/admin";
    }
}
