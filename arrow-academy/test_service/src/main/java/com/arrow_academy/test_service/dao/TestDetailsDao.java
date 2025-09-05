package com.arrow_academy.test_service.dao;

import com.arrow_academy.test_service.model.TestDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TestDetailsDao extends JpaRepository<TestDetails, Integer> {

    Optional<TestDetails> findByTitleAndDate(String title, Date date);
}
