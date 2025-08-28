package com.arrow_academy.test_service.dao;

import com.arrow_academy.test_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionDao extends JpaRepository<Question, Integer> {
}
