package com.newtwitter.controller;

import com.newtwitter.model.Message;
import com.newtwitter.model.User;
import com.newtwitter.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    private final MessageRepository messageRepository;

    public MainController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false) String tag, Model model) {
        if (tag != null && !tag.isEmpty()) {
            model.addAttribute("messages", messageRepository.findByTag(tag));
        } else {
            model.addAttribute("messages", messageRepository.findAll());
        }
        model.addAttribute("tag", tag);
        return "main";
    }

    @PostMapping("/main")
    public String addMessage(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            @RequestParam("file") MultipartFile file,
            Model model
    ) throws IOException {
        Message message = new Message(text, tag, user);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            message.setFileName(saveFile(file));
        }

        messageRepository.save(message);
        model.addAttribute("messages", messageRepository.findAll());
        return "main";
    }

    /**
     * Save Multipart File.
     * @param file MultipartFile.
     * @return saved fileName.
     * @throws IOException ioe.
     */
    private String saveFile(MultipartFile file) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String uuidFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + uuidFileName));
        return uuidFileName;
    }

}
