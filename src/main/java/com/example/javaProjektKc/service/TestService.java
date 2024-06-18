package com.example.javaProjektKc.service;

import com.example.javaProjektKc.entity.Question;
import com.example.javaProjektKc.entity.User;
import com.example.javaProjektKc.repository.QuestionRepository;
import com.example.javaProjektKc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;


    public Optional<Question> getNextQuestion(Long currentQuestionId) {
        Long nextQuestionId = currentQuestionId != null ? currentQuestionId + 1 : getFirstQuestionId();
        while (true) {
            Optional<Question> question = questionRepository.findById(nextQuestionId);
            if (question.isPresent()) {
                return question;
            }
            nextQuestionId++;
            if (!questionRepository.existsById(nextQuestionId)) {
                return Optional.empty();
            }
        }
    }
    private Long getFirstQuestionId() {
        return questionRepository.findAll()
                .stream()
                .map(Question::getId)
                .min(Long::compareTo)
                .orElse(null);
    }

    public void updateScore(int points) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        user.setScore(user.getScore() + points);
        user.setFinishedTest(true);
        userRepository.save(user);
    }
}