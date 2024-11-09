package com.example.demo.post.controller;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PostCreateControllerTest {

    @Test
    void 사용자는_게시물을_작성할수_있다() {
        TestContainer testContainer = TestContainer.create(() -> 1236879L, () -> "aaaaaa-aaaaa-aaaaaaaa");
        testContainer.userRepository.save(User.builder()
                .id(1000L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaa-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build());

        PostCreate postCreate = PostCreate.builder()
                .writerId(1000)
                .content("내용!!")
                .build();

        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("내용!!");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(1236879L);
        assertThat(result.getBody().getModifiedAt()).isNull();
        assertThat(result.getBody().getWriter().getId()).isEqualTo(1000L);
        assertThat(result.getBody().getWriter().getNickname()).isEqualTo("테스터");
    }

}