package com4table.ssupetition.domain.login.service;

import com4table.ssupetition.domain.user.domain.User;
import com4table.ssupetition.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final USaintCrawler uSaintCrawler;

    public User login(String id, String password) throws IOException {
        // 데이터베이스에서 유저 검색
        User user = userRepository.findByLoginId(id).orElse(null);

        if (user == null) {
            log.info("user 존재하지 않음");


            // 유저가 존재하지 않으면 유세인트에서 정보 크롤링
            USaintCrawler.UserInfo userInfo = uSaintCrawler.loginAndGetInfo(id, password).getUserInfo();

            // 새로운 유저 생성 및 저장
            if (userInfo != null) {
                user = User.builder()
                        .loginId(userInfo.getLoginId())
                        .major(userInfo.getUserMajor())
                        .name(userInfo.getUserName())
                        .build();
//                user = User.builder()
//                        .loginId(userInfo.getLoginId())
//                        .major(userInfo.getUserMajor())
//                        .name(userInfo.getUserName())
//                        .build();
                userRepository.save(user);
            } else {
                log.info("로그인 실패");
                return null;
            }
        } else {
            log.info("user 존재");
            if(!uSaintCrawler.userCheck(id, password)){
                log.info("비밀번호 틀림");
                return null;
            }
        }

        // 세션을 통한 로그인 로직 (생략)
        return user;
    }
}
