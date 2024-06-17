package com.example.javaProjektKc;

import com.example.javaProjektKc.entity.User;
import com.example.javaProjektKc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSetup implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("jprokop@prz.edu.pl") == null) {
            User admin = new User();
            admin.setEmail("jprokop@prz.edu.pl");
            admin.setPassword(passwordEncoder.encode("TestAdministrator1JP"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
    }
}
