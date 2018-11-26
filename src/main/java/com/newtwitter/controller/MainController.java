package com.newtwitter.controller;

import com.newtwitter.model.Message;
import com.newtwitter.model.User;
import com.newtwitter.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private MessageRepository repository;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false) String tag, Model model) {
        if (tag != null && !tag.isEmpty()) {
            model.addAttribute("messages", repository.findByTag(tag));
        } else {
            model.addAttribute("messages", repository.findAll());
        }
        model.addAttribute("tag", tag);
        return "main";
    }

    @PostMapping("/main")
    public String putMessage(
            @AuthenticationPrincipal User user,
            @RequestParam String message,
            @RequestParam String tag, Model model
    ) {
        repository.save(new Message(message, tag, user));
        model.addAttribute("messages", repository.findAll());
        return "main";
    }

}
