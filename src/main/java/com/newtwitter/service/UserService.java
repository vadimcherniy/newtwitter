package com.newtwitter.service;

import com.newtwitter.model.Role;
import com.newtwitter.model.User;
import com.newtwitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Value("${domain.path}")
    private String domain;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUsername(userName);
    }

    /**
     * Register new user.
     *
     * @return true is user was register, or else false.
     */
    public boolean registerUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        modifyUser(user);
        userRepository.save(user);
        sendActivationCode(user);
        return true;
    }

    /**
     * Activate user account by token;
     *
     * @param code from email.
     * @return boolean value.
     */
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    /**
     * Save user.
     */
    public void userSave(String name, Map<String, String> form, User user) {
        user.setUsername(name);
        user.getRoles().clear();
        Set<String> roles = Arrays.stream(Role.values()).map(Enum::name).collect(Collectors.toSet());
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    /**
     * Update user.
     */
    public void updateProfile(User user, String password, String email) {
        boolean isEmailChanged = email != null && !email.isEmpty() && !email.equals(user.getEmail());

        if (isEmailChanged) {
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
        }

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);

        if (isEmailChanged) {
            sendActivationCode(user);
        }
    }

    /**
     * Get all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    private void sendActivationCode(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to NewTwitter. Please, verify your account.\n" +
                            "Visit this link: " + domain + "/activate/%s",
                    user.getUsername(), user.getActivationCode()
            );

            mailService.send(user.getEmail(), "Activate your account", message);
        }
    }

    /**
     * Modify user object.
     */
    private void modifyUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
    }
}
