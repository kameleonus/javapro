package com.example.javaProjektKc.repository;

import com.example.javaProjektKc.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}