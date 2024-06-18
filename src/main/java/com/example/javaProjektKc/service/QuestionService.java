package com.example.javaProjektKc.service;

import com.example.javaProjektKc.entity.Question;
import com.example.javaProjektKc.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }
    public void deleteQuestionById(Long id) {
        questionRepository.deleteById(id);
    }
    @Transactional
    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }
}