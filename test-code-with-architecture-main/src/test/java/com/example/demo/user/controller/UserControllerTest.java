package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_전달받을_수_있다_단_주소지같은_개인정보는_제외된다(){
        UserReadService mockUserReadService = new UserReadService() {
            @Override
            public User getByEmail(String email) {
                return null;
            }

            @Override
            public User getById(long id) {
                return User.builder()
                        .id(id)
                        .email("test@naver.com")
                        .nickname("테스터")
                        .address("서울시 강남구")
                        .status(UserStatus.ACTIVE)
                        .certificationCode("aaaaaa-aaaaa-aaaaaaaa")
                        .lastLoginAt(0L)
                        .build();
            }
        };

        UserController userController = UserController.builder()
                .userReadService(mockUserReadService).build();

        ResponseEntity<UserResponse> result = userController.getUserById(1L);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("테스터");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_특정_유저의_정보를_전달받을_수_있다_단_주소지같은_개인정보는_제외된다_컨테이너_테스트(){
        ClockHolder clockHolder = null;
        UuidHolder uuidHolder = null;
        TestContainer testContainer = TestContainer.create(clockHolder, uuidHolder);
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaa-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build());

        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1L);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("테스터");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }


    @Test
    void 존재하지않는_사용자를_조회하면_404_응답을_받는다(){
        UserReadService mockUserReadService = new UserReadService() {
            @Override
            public User getByEmail(String email) {
                return null;
            }

            @Override
            public User getById(long id) {
                throw new ResourceNotFoundException("Users", id);
            }
        };

        UserController userController = UserController.builder()
                .userReadService(mockUserReadService).build();
        assertThatThrownBy(() -> userController.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
    
    @Test
    void 존재하지않는_사용자를_조회하면_404_응답을_받는다_컨테이너_테스트(){
        ClockHolder clockHolder = null;
        UuidHolder uuidHolder = null;
        TestContainer testContainer = TestContainer.create(clockHolder, uuidHolder);
        testContainer.userRepository.save(User.builder()
                .id(2L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaa-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build());

        assertThatThrownBy(() -> testContainer.userController.getUserById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_할수있다() throws Exception {
        ClockHolder clockHolder = null;
        UuidHolder uuidHolder = null;
        TestContainer testContainer = TestContainer.create(clockHolder, uuidHolder);
        testContainer.userRepository.save(User.builder()
                .id(1001L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.PENDING)
                .certificationCode("BBBBB-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build());

        ResponseEntity<Void> response = testContainer.userController.verifyEmail(1001L, "BBBBB-aaaaa-aaaaaaaa");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(testContainer.userRepository.getById(1001L).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 인증_코드로_인증실패시_403_fobidden을_응답받는다() throws Exception {
        ClockHolder clockHolder = null;
        UuidHolder uuidHolder = null;
        TestContainer testContainer = TestContainer.create(clockHolder, uuidHolder);
        testContainer.userRepository.save(User.builder()
                .id(1001L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.PENDING)
                .certificationCode("BBBBB-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build());

        assertThatThrownBy(() -> testContainer.userController.verifyEmail(1001L, "xxxxxxxxxxxx-aaaaa-aaaaaaaa"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 내정보를_조회할_수_있다() throws Exception {
        ClockHolder clockHolder = new TestClockHolder(1698765432L);
        UuidHolder uuidHolder = null;
        TestContainer testContainer = TestContainer.create(clockHolder, uuidHolder);
        testContainer.userRepository.save(User.builder()
                .id(1000L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaa-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build());

        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("test@naver.com");
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(1000L);
        assertThat(result.getBody().getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("테스터");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(1698765432L);
    }

    @Test
    void 내정보를_수정할_수_있다() throws Exception {
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("테스터_수정")
                .address("서울시 강북구")
                .build();

        ClockHolder clockHolder = null;
        UuidHolder uuidHolder = null;
        TestContainer testContainer = TestContainer.create(clockHolder, uuidHolder);
        testContainer.userRepository.save(User.builder()
                .id(1000L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaa-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build());

        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("test@naver.com", userUpdate);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(1000L);
        assertThat(result.getBody().getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("테스터_수정");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(0);
        assertThat(result.getBody().getAddress()).isEqualTo("서울시 강북구");
    }

}