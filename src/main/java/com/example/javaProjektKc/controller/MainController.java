package com.example.javaProjektKc.controller;


import com.example.javaProjektKc.entity.User;
import com.example.javaProjektKc.repository.UserRepository;
import com.example.javaProjektKc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/test")
    public String testPage() {
        return "test";
    }
    @GetMapping("/question-add")
    public String questionAddPage() {
        return "question-add";
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.userForm", "Email already in database!");
            return "register";
        }

        // Create user entity from userForm
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole());

        // Save user to database
        userService.createUser(newUser);

        // Redirect to login page or any other page
        return "redirect:/login";
    }

}