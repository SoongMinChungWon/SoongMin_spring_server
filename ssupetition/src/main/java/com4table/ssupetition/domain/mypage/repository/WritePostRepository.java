package com4table.ssupetition.domain.mypage.repository;

import com4table.ssupetition.domain.mypage.domain.AgreePost;
import com4table.ssupetition.domain.mypage.domain.WritePost;
import com4table.ssupetition.domain.user.domain.User;
import lombok.Locked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WritePostRepository extends JpaRepository<WritePost, Long> {
    List<WritePost> findByUser(User user);
}
