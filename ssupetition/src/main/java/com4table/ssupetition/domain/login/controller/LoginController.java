package com4table.ssupetition.domain.login.controller;

import com4table.ssupetition.domain.login.dto.LoginRequest;
import com4table.ssupetition.domain.login.service.LoginService;
import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.dto.PostRequest;
import com4table.ssupetition.domain.post.service.PostService;
import com4table.ssupetition.domain.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Tag(name = "로그인 API", description = "로그인 API에 대한 설명입니다.")
public class LoginController {

    private final LoginService loginService;


    @Operation(description = "ID와 비밀번호를 입력받아서 백엔드에서 usaint 접속 후 로그인 함 -> 만약 안되면 그냥 로그인 부분 넘기삼")
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