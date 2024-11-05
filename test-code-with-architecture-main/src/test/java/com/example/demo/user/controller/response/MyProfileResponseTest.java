package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyProfileResponseTest {
    @Test
    public void User로부터_UserResponse로_변환할_수_있다() {
        // given
        User writer = User.builder()
                .id(1000L)
                .email("test@naver.com")
                .nickname("test")
                .address("강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaa-aaaaa-aaaaaaaa")
                .lastLoginAt(300L)
                .build();

        // when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(writer);

        // then
        assertThat(myProfileResponse.getId()).isEqualTo(1000L);
        assertThat(myProfileResponse.getEmail()).isEqualTo("test@naver.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("test");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponse.getAddress()).isEqualTo("강남구");
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(300L);
    }
}