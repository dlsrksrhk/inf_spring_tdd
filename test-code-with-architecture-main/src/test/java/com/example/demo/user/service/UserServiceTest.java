package com.example.demo.user.service;

import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserEntity;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql"),
        @Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender mailSender;


    @Test
    void getById은_Active_상태의_사용자를_가져온다() {
        Long id = 1000L;
        UserEntity result = userService.getById(id);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void getById은_Active가_아닌_사용자를_가져오면_예외를던진다() {
        Long id = 1001L;
        assertThatThrownBy(() -> userService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getByEmail은_Active_상태의_사용자를_가져온다() {
        String email = "test@naver.com";
        UserEntity result = userService.getByEmail(email);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void getByEmail은_Active가_아닌_사용자를_가져오면_예외를던진다() {
        String email = "test2@naver.com";
        assertThatThrownBy(() -> userService.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }


    @Test
    void UserCreate를_이용하여_유저를_생성할수있다() {
        UserCreate userCreate = UserCreate.builder()
                .email("test3@kakao.com")
                .address("서울시 강남구")
                .nickname("테스트3")
                .build();

        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        UserEntity result = userService.create(userCreate);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getEmail()).isEqualTo(userCreate.getEmail());
        assertThat(result.getAddress()).isEqualTo(userCreate.getAddress());
        assertThat(result.getNickname()).isEqualTo(userCreate.getNickname());
    }

    @Test
    void UserUpdate를_이용하여_유저_닉네임_주소를_변경_할수있다() {
        UserUpdate userUpdate = UserUpdate.builder()
                .address("서울시 강남구_변경")
                .nickname("테스트3")
                .build();

        UserEntity result = userService.update(1000L, userUpdate);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getAddress()).isEqualTo(userUpdate.getAddress());
        assertThat(result.getNickname()).isEqualTo(userUpdate.getNickname());
    }

    @Test
    void 로그인시키면_마지막_로그인_시간이_변경된다() {
        userService.login(1000L);

        UserEntity result = userService.getById(1000L);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getLastLoginAt()).isGreaterThan(0L);
    }

    @Test
    void 인증상태가_PENDING인_사용자는_인증코드로_ACTIVE_시킬수있다() {
        userService.verifyEmail(1001L, "BBBBB-aaaaa-aaaaaaaa");

        UserEntity result = userService.getById(1001L);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 인증코드가_맞지않으면_인증에_실패하고_예외가발생한다() {
        assertThatThrownBy(()->userService.verifyEmail(1001L, "xxxxx-aaaaa-aaaaaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}