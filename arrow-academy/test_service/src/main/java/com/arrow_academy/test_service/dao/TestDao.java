package com.arrow_academy.test_service.dao;

import com.arrow_academy.test_service.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestDao extends JpaRepository<Test, Integer> {
    List<Test> findByTestDetailsId(Integer testDetailsId);
}
