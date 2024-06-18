package com.example.javaProjektKc.controller;

import com.example.javaProjektKc.entity.Question;
import com.example.javaProjektKc.entity.Answer;
import com.example.javaProjektKc.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/add-question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    @GetMapping
    public String showAddQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        return "add-question";
    }


    @PostMapping
    public String addQuestion(@RequestParam("text") String text,
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
        Question question = new Question();
        question.setText(text);

        if (!image.isEmpty()) {
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
            question.setImage(base64Image);
        }

        question.setCodeSnippet(codeSnippet);

        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer(answer1Text, answer1Correct, question));
        answers.add(new Answer(answer2Text, answer2Correct, question));
        answers.add(new Answer(answer3Text, answer3Correct, question));
        answers.add(new Answer(answer4Text, answer4Correct, question));

        question.setAnswers(answers);


        boolean hasCorrectAnswer = answer1Correct || answer2Correct || answer3Correct || answer4Correct;

        if (!hasCorrectAnswer) {
            redirectAttributes.addFlashAttribute("error", "At least one answer must be marked as correct.");
            return "redirect:/add-question";
        }


        questionService.saveQuestion(question);

        return "redirect:/add-question";
    }
}