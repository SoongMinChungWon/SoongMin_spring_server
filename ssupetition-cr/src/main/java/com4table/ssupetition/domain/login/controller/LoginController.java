package com4table.ssupetition.domain.login.controller;

import com4table.ssupetition.domain.login.dto.LoginRequest;
import com4table.ssupetition.domain.login.service.LoginService;
import com4table.ssupetition.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<User> login(@RequestBody LoginRequest.LoginDTO loginDTO) {
        try {
            User user = loginService.login(loginDTO.getId(), loginDTO.getPassword());
            if(user==null){
                return ResponseEntity.status(500).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}