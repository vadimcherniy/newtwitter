package com.newtwitter.controller;

import com.newtwitter.model.User;
import com.newtwitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        if (user.getPassword() != null && !user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError","Passwords must be the same");
        }

        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.getErrors(bindingResult));
            return "registration";
        }

        if (!userService.registerUser(user)) {
            model.addAttribute("usernameError", "User is exists");
            return "registration";
        }
            return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activateUserAccount(@PathVariable String code, Model model) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found");
        }
        return "login";
    }

}
