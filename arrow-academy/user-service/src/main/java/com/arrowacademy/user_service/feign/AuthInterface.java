package com.arrowacademy.user_service.feign;

import com.arrowacademy.user_service.model.User;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTH-SERVICE", url = "${auth.service.url}" )
public interface AuthInterface {

    @PostMapping("/admin/register")
    public ResponseEntity<String> adminRegister(@Valid @RequestBody User user);
}
