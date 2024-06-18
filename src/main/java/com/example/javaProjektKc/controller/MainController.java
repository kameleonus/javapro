package com.example.javaProjektKc.controller;


import com.example.javaProjektKc.entity.Question;
import com.example.javaProjektKc.entity.User;
import com.example.javaProjektKc.repository.UserRepository;
import com.example.javaProjektKc.service.QuestionService;
import com.example.javaProjektKc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class MainController {
    @Autowired
    private UserService userService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private QuestionService questionService;

    //Część studenta

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
        }
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

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole());

        userService.createUser(newUser);

        return "redirect:/login";
    }
    //Dla ADMIN
    @GetMapping("/questions")
    public String showQuestions(Model model) {
        List<Question> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        return "questions";
    }
    @PostMapping("/delete-question/{id}")
    public String deleteQuestion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        questionService.deleteQuestionById(id);
        redirectAttributes.addFlashAttribute("message", "Question deleted successfully.");
        return "redirect:/questions";
    }
    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
    @PostMapping("/delete-user/{id}")
    @Secured("ADMIN")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully.");
        return "redirect:/users";
    }

    @GetMapping("/question-add")
    public String questionAddPage() {
        return "question-add";
    }




}