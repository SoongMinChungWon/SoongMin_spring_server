package com4table.ssupetition.domain.post.service;

import com4table.ssupetition.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void addPost(){

    }

    public void removePost(){

    }


    public void addPostAgree(){

    }

    public void addPostDisagree(){

    }

    public void getPostList(){

    }
    public void getPostListWithCategory(){

    }

    public void getPostListWithPostType(){

    }

    //최다동의순
    //만료임박순
    //최신순
    //알림 만들어야됨






}
