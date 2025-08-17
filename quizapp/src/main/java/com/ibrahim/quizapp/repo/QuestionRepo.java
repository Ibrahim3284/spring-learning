package com.ibrahim.quizapp.repo;

import com.ibrahim.quizapp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(String category);
}
