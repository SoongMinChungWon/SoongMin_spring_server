package com4table.ssupetition.domain.post.service;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.domain.PostAnswer;
import com4table.ssupetition.domain.post.repository.PostAnswerRepository;
import com4table.ssupetition.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostAnswerService {
    private PostAnswerRepository postAnswerRepository;
    private PostRepository postRepository;

    public void savePostAnswer(Long postId, String content) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            PostAnswer postAnswer = PostAnswer.builder()
                    .postId(post)
                    .postAnswerContent(content)
                    .build();
            postAnswerRepository.save(postAnswer);
        }
    }
}
