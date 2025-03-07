package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userHome(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username);
        Set<Role> roles = user.getRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return "user";
    }
}

