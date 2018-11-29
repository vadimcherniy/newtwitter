package com.newtwitter.controller;

import com.newtwitter.model.Role;
import com.newtwitter.model.User;
import com.newtwitter.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model
    ) {
        User userFromDb = userRepository.findByName(username);
        if (userFromDb != null) {
            model.addAttribute("message", "User is exists");
            return "registration";
        }
        userRepository.save(new User(username, password, true, Collections.singleton(Role.USER)));
        return "redirect:/login";
    }
}
