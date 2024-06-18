package com.example.javaProjektKc.controller;

import com.example.javaProjektKc.entity.Question;
import com.example.javaProjektKc.entity.Answer;
import com.example.javaProjektKc.entity.User;
import com.example.javaProjektKc.repository.UserRepository;
import com.example.javaProjektKc.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class TestController {

    @Autowired
    private TestService testService;
    @Autowired
    private UserRepository userRepository;
    private int points;
    @GetMapping("/start-test")
    public String startTest(Model model, @RequestParam(required = false) Long questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User user = userRepository.findByEmail(name);
        if (user.isFinishedTest()) {
            return "test-completed";
        }
        Optional<Question> question = testService.getNextQuestion(questionId);
        if (question.isPresent()) {
            model.addAttribute("question", question.get());
            return "test";
        } else {
            testService.updateScore(points);
            return "test-completed";
        }
    }

    @PostMapping("/submit-answer")
    public String submitAnswer(@RequestParam Long questionId, @RequestParam(required = false) List<Long> answerIds) {
        if (answerIds == null || answerIds.isEmpty()) {
            // Jakby ktoś nic nie zaznaczył
            return "redirect:/start-test?questionId=" + questionId;
        }

        Optional<Question> optionalQuestion = testService.getNextQuestion(questionId - 1);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();

            boolean allCorrectAnswersIncluded = question.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .allMatch(answer -> answerIds.contains(answer.getId()));

            points += allCorrectAnswersIncluded ? 1 : 0;

        }

        return "redirect:/start-test?questionId=" + questionId;
    }
}