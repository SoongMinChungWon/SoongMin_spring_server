package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findByPostCategory(Long postCategoryId);
//    List<Post> findByPostType(Long postCategoryId);


    // 최다 동의 순으로 정렬
    @Query("SELECT p FROM Post p ORDER BY p.agree DESC")
    List<Post> findAllOrderByAgreeDesc();

    // 최다 동의 순으로 정렬(카테고리와 타입 적용)
    List<Post> findByPostCategoryAndPostTypeOrderByAgreeDesc(Category category, Type type);

    // 만료 임박 순으로 정렬
    @Query("SELECT p FROM Post p ORDER BY p.createdAt ASC")
    List<Post> findAllOrderByExpiryAsc();

    // 만료 임박 순으로 정렬(카테고리와 타입 적용)
    List<Post> findByPostCategoryAndPostTypeOrderByCreatedAtAsc(Category category, Type type);

    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllOrderByCreatedDateDesc();

    // 최신 순으로 정렬(카테고리와 타입 적용)
    List<Post> findByPostCategoryAndPostTypeOrderByCreatedAtDesc(Category category, Type type);

}

