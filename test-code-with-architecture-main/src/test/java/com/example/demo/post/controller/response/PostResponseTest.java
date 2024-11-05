package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostResponseTest {

    @Test
    void Post로부터_PostResponse를_생성할_수_있다() {
        User writer = User.builder()
                .id(1000L)
                .email("test@naver.com")
                .nickname("test")
                .address("강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaa-aaaaa-aaaaaaaa")
                .build();

        Post post = Post.builder()
                .id(1000L)
                .content("content")
                .createdAt(1000L)
                .modifiedAt(1000L)
                .writer(writer)
                .build();

        PostResponse postResponse = PostResponse.from(post);

        assertThat(postResponse.getId()).isEqualTo(1000L);
        assertThat(postResponse.getContent()).isEqualTo("content");
        assertThat(postResponse.getCreatedAt()).isEqualTo(1000L);
        assertThat(postResponse.getModifiedAt()).isEqualTo(1000L);
        assertThat(postResponse.getWriter().getId()).isEqualTo(1000L);
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("test@naver.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("test");
    }

}