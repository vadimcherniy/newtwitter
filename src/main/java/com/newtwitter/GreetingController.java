package com.newtwitter;

import com.newtwitter.Model.Message;
import com.newtwitter.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class GreetingController {

    @Autowired
    private MessageRepository repository;

    @GetMapping
    public String main(Model model) {
        model.addAttribute("messages", repository.findAll());
        return "index";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("messages", name);
        return "greeting";
    }

    @PostMapping
    public String putMessage(@RequestParam String message, @RequestParam String tag, Model model) {
        repository.save(new Message(message, tag));
        model.addAttribute("messages", repository.findAll());
        return "index";
    }
}
