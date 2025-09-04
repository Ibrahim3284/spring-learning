package com.arrow_academy.test_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE", url = "${user.service.url}")
public interface UserInterface {

    @GetMapping("/student/getStudentId")
    public ResponseEntity<Integer> getStudentId(@RequestHeader("Authorization") String token);
}
