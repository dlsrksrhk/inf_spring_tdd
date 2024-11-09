package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostControllerTest {

    @Test
    void 사용자는_게시물_하나를_조회할수_있다() {
        TestContainer testContainer = TestContainer.create(() -> 1236879L, () -> "aaaaaa-aaaaa-aaaaaaaa");
        User user = User.builder()
                .id(1000L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaa-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build();

        User savedUser = testContainer.userRepository.save(user);

        Post post = testContainer.postRepository.save(Post.builder()
                .id(1000L)
                .content("First post content")
                .createdAt(1698765432L)
                .modifiedAt(1698765432L)
                .writer(savedUser)
                .build());

        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1000L);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(1000L);
        assertThat(result.getBody().getContent()).isEqualTo("First post content");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1698765432L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(1698765432L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1000L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("테스터");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@naver.com");
    }

    @Test
    void 사용자는_게시물의_내용을_수정할수_있다_수정되면_수정일시가_바뀐다() {
        TestContainer testContainer = TestContainer.create(() -> 1236879L, () -> "aaaaaa-aaaaa-aaaaaaaa");
        User user = User.builder()
                .id(1000L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaa-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build();

        User savedUser = testContainer.userRepository.save(user);

        Post post = testContainer.postRepository.save(Post.builder()
                .id(1000L)
                .content("First post content")
                .createdAt(0L)
                .modifiedAt(0L)
                .writer(savedUser)
                .build());

        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1000L, PostUpdate.builder()
                .content("Updated post content")
                .build());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(1000L);
        assertThat(result.getBody().getContent()).isEqualTo("Updated post content");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(0L);
        assertThat(result.getBody().getModifiedAt()).isEqualTo(1236879L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1000L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("테스터");
        assertThat(result.getBody().getWriter().getEmail()).isEqualTo("test@naver.com");
    }

}