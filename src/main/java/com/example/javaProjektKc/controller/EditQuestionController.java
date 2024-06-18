package com.example.javaProjektKc.controller;

import com.example.javaProjektKc.entity.Question;
import com.example.javaProjektKc.entity.Answer;
import com.example.javaProjektKc.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/edit-question")
public class EditQuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/{id}")
    public String showEditQuestionForm(@PathVariable Long id, Model model) {
        Optional<Question> question = questionService.getQuestionById(id);
        if (question.isPresent()) {
            model.addAttribute("question", question.get());
            return "edit-question";
        } else {
            return "redirect:/questions";
        }
    }

    @PostMapping("/{id}")
    public String editQuestion(@PathVariable Long id,
                               @RequestParam("text") String text,
                               @RequestParam("image") MultipartFile image,
                               @RequestParam("codeSnippet") String codeSnippet,
                               @RequestParam("answers[0].text") String answer1Text,
                               @RequestParam(value = "answers[0].correct", defaultValue = "false") boolean answer1Correct,
                               @RequestParam("answers[1].text") String answer2Text,
                               @RequestParam(value = "answers[1].correct", defaultValue = "false") boolean answer2Correct,
                               @RequestParam("answers[2].text") String answer3Text,
                               @RequestParam(value = "answers[2].correct", defaultValue = "false") boolean answer3Correct,
                               @RequestParam("answers[3].text") String answer4Text,
                               @RequestParam(value = "answers[3].correct", defaultValue = "false") boolean answer4Correct,
                               RedirectAttributes redirectAttributes) throws IOException {
        Optional<Question> optionalQuestion = questionService.getQuestionById(id);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            question.setText(text);

            if (!image.isEmpty()) {
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                question.setImage(base64Image);
            }

            question.setCodeSnippet(codeSnippet);

            question.getAnswers().clear();

            List<Answer> answers = new ArrayList<>();
            answers.add(new Answer(answer1Text, answer1Correct, question));
            answers.add(new Answer(answer2Text, answer2Correct, question));
            answers.add(new Answer(answer3Text, answer3Correct, question));
            answers.add(new Answer(answer4Text, answer4Correct, question));

            question.getAnswers().addAll(answers);

            boolean hasCorrectAnswer = answer1Correct || answer2Correct || answer3Correct || answer4Correct;

            if (!hasCorrectAnswer) {
                redirectAttributes.addFlashAttribute("error", "At least one answer must be marked as correct.");
                return "redirect:/edit-question?id=" + id;
            }
            questionService.saveQuestion(question);
        }


        return "redirect:/questions";
    }
}