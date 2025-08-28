package com.arrow_academy.test_service.dao;

import com.arrow_academy.test_service.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDao extends JpaRepository<Test, Integer> {
    Test findByTestTitleAndSubjectName(String testTitle, String subjectName);
}
