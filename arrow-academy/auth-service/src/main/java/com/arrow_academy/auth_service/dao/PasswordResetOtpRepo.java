package com.arrow_academy.auth_service.dao;

import com.arrow_academy.auth_service.model.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetOtpRepo extends JpaRepository<PasswordResetOtp, Integer> {
    Optional<PasswordResetOtp> findByOtp(Integer token);

    Optional<PasswordResetOtp> findByEmail(String username);
}
