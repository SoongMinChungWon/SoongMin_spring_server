package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.mypage.domain.CommentPost;
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
    List<Post> findByUser_UserId(Long user);




    // 최다 동의 순으로 정렬
    @Query("SELECT p FROM Post p ORDER BY p.agree DESC")
    List<Post> findAllOrderByAgreeDesc();

    // 최다 동의 순으로 정렬(카테고리와 타입 적용)
    List<Post> findByPostCategoryAndPostTypeOrderByAgreeDesc(Category category, Type type);

    List<Post> findByPostTypeOrderByAgreeDesc(Type type);


    // 만료 임박 순으로 정렬
    @Query("SELECT p FROM Post p ORDER BY p.createdAt ASC")
    List<Post> findAllOrderByExpiryAsc();

    // 만료 임박 순으로 정렬(카테고리와 타입 적용)
    List<Post> findByPostCategoryAndPostTypeOrderByCreatedAtAsc(Category category, Type type);
    List<Post> findByPostTypeOrderByCreatedAtAsc(Type type);


    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllOrderByCreatedDateDesc();

    // 최신 순으로 정렬(카테고리와 타입 적용)
    List<Post> findByPostCategoryAndPostTypeOrderByCreatedAtDesc(Category category, Type type);
    List<Post> findByPostTypeOrderByCreatedAtDesc(Type type);


    // 카테고리에 따라 정렬된 게시물 조회
    List<Post> findByPostCategoryOrderByAgreeDesc(Category category);

    List<Post> findByPostCategoryOrderByCreatedAtAsc(Category category);

    List<Post> findByPostCategoryOrderByCreatedAtDesc(Category category);

    //알림용 조회
    @Query("SELECT p FROM Post p WHERE p.user.userId = :userId AND p.postType IN :types ORDER BY p.updateAt DESC")
    List<Post> findByUserIdAndPostTypeInOrderByUpdateAtDesc(@Param("userId") Long userId, @Param("types") List<Type> types);

    //타입별 조회(state1, state2 합쳐서 조회)
    List<Post> findByPostTypeInOrderByAgreeDesc(List<Type> postTypes);
    List<Post> findByPostTypeInOrderByCreatedAtAsc(List<Type> postTypes);
    List<Post> findByPostTypeInOrderByCreatedAtDesc(List<Type> postTypes);
}




