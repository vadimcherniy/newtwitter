package com.newtwitter.controller;

import com.newtwitter.model.Role;
import com.newtwitter.model.User;
import com.newtwitter.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @GetMapping(value = "/{user}")
    public String edit(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String save(
            @RequestParam String name,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        user.setName(name);
        Set<String> roles = Arrays.stream(Role.values()).map(Enum::name).collect(Collectors.toSet());
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
        return "redirect:/user";
    }
}
