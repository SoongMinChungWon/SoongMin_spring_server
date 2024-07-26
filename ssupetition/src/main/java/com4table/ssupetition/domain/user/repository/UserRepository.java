package com4table.ssupetition.domain.user.repository;

import com4table.ssupetition.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
