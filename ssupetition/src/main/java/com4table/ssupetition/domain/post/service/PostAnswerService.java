package com4table.ssupetition.domain.post.service;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.domain.PostAnswer;
import com4table.ssupetition.domain.post.dto.PostResponse;
import com4table.ssupetition.domain.post.repository.PostAnswerRepository;
import com4table.ssupetition.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostAnswerService {
    private final PostAnswerRepository postAnswerRepository;
    private final PostRepository postRepository;

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

    public List<PostResponse.PostAnswerDTO> getAnswersWithPostId(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return List.of(); // 혹은 예외 처리
        }

        Post post = postOptional.get();
        List<PostAnswer> answers = postAnswerRepository.findByPostId_PostId(postId);

        return answers.stream()
                .map(answer -> new PostResponse.PostAnswerDTO(post, answer))
                .collect(Collectors.toList());
    }
}
