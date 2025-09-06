package com.arrow_academy.auth_service.services;

import com.arrow_academy.auth_service.dao.PasswordResetOtpRepo;
import com.arrow_academy.auth_service.dao.UserRepo;
import com.arrow_academy.auth_service.model.PasswordResetOtp;
import com.arrow_academy.auth_service.model.ResetPassword;
import com.arrow_academy.auth_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
public class PasswordResetService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private PasswordResetOtpRepo passwordResetOtpRepo;

    public ResponseEntity<?> sendEmail(String email) {
        List<User> userOptional = repo.findAllByUsername(email);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        User user = userOptional.getFirst();
        int otp = 1000 + new Random().nextInt(9000);

        if(passwordResetOtpRepo.findByEmail(email).isEmpty()) {
            PasswordResetOtp resetOtp = new PasswordResetOtp();
            resetOtp.setOtp(otp);
            resetOtp.setEmail(email);
            resetOtp.setExpiryTime(Instant.now().plusSeconds(86400)); // 15 minutes validity
            resetOtp.setConsumed(false);

            passwordResetOtpRepo.save(resetOtp);
        } else {
            PasswordResetOtp resetOtp = passwordResetOtpRepo.findByEmail(email).get();
            resetOtp.setOtp(otp);
            resetOtp.setExpiryTime(Instant.now().plusSeconds(86400));
            resetOtp.setConsumed(false);

            passwordResetOtpRepo.save(resetOtp);
        }
        sendPasswordResetEmail(email, otp);

        return ResponseEntity.ok("Reset email sent");
    }

    public void sendPasswordResetEmail(String toEmail, Integer token) {
        String subject = "Password Reset OTP";
        String body = "Use the following OTP to reset the password: " + token.toString();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public ResponseEntity<?> resetPassword(ResetPassword resetPassword, int otp) {

        if(repo.findAllByUsername(resetPassword.getUsername()).isEmpty() || passwordResetOtpRepo.findByEmail(resetPassword.getUsername()).isEmpty()) return new ResponseEntity<>("User doesnt exist or otp expired", HttpStatus.NOT_FOUND);

        if(passwordResetOtpRepo.findByOtp(otp).isEmpty() || !(passwordResetOtpRepo.findByEmail(resetPassword.getUsername()).get().getOtp() == otp)) return new ResponseEntity<>("OTP doesnt exist or otp doesnt match", HttpStatus.NOT_FOUND);

        if(passwordResetOtpRepo.findByOtp(otp).get().getExpiryTime().isAfter(Instant.now())) {

            if(!resetPassword.getConfirmPassword().equals(resetPassword.getPassword())) return new ResponseEntity<>("Confirm password field doesnt match with password field", HttpStatus.BAD_REQUEST);

            User user = repo.findByUsername(resetPassword.getUsername());
            user.setPassword(encoder.encode(resetPassword.getPassword()));
            passwordResetOtpRepo.deleteById(passwordResetOtpRepo.findByEmail(resetPassword.getUsername()).get().getId());
            repo.save(user);

            return new ResponseEntity<>("Password reset successful", HttpStatus.OK);
        } else {
            passwordResetOtpRepo.deleteById(passwordResetOtpRepo.findByEmail(resetPassword.getUsername()).get().getId());
            return new ResponseEntity<>("OTP has expired.", HttpStatus.UNAUTHORIZED);
        }

    }
}
