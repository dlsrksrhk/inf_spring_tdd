package com.example.demo.user.controller;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {

    @Test
    void 사용자는_회원가입을_할수있고_회원가입직후에_상태는_PENDING_상태이다() {
        ClockHolder clockHolder = null;
        UuidHolder uuidHolder = new TestUuidHolder("aaaaaa-aaaaa-aaaaaaaa");
        TestContainer testContainer = TestContainer.create(clockHolder, uuidHolder);

        UserCreate userCreate = UserCreate.builder()
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .build();

        ResponseEntity<UserResponse> result = testContainer.userCreateController.createUser(userCreate);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("테스터");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
    }
}