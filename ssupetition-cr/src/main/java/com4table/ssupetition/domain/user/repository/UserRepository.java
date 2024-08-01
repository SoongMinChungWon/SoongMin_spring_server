package com4table.ssupetition.domain.user.repository;

import com4table.ssupetition.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //로그인
    Optional<User> findByLoginId(String loginId);


}
