package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    public void User는_UserCreate로부터_생성될_수_있다() {
        // given with builder
        UserCreate userCreate = UserCreate.builder()
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .build();

        // when
        TestUuidHolder testUuidHolder = new TestUuidHolder("aaaaa-aaaaa-aaaaaaaa");
        User user = User.from(userCreate, testUuidHolder);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@naver.com");
        assertThat(user.getNickname()).isEqualTo("테스터");
        assertThat(user.getAddress()).isEqualTo("서울시 강남구");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isNotNull();//TODO: 랜덤 문자열 생성 검증은 어떻게..? 현재는 NULL여부만 체크
        assertThat(user.getCertificationCode()).isEqualTo(testUuidHolder.random());//TODO: UUID 생성을 추상화하여 테스트용 스텁으로 대체
    }

    @Test
    public void User는_UserUpdate로부터_수정될_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .certificationCode("aaaaa-aaaaa-aaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();

        UserUpdate userUpdate = new UserUpdate("새로운 닉네임", "새로운 주소");

        // when
        User updatedUser = user.update(userUpdate);

        // then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isEqualTo(1L);
        assertThat(updatedUser.getNickname()).isEqualTo("새로운 닉네임");
        assertThat(updatedUser.getAddress()).isEqualTo("새로운 주소");
        assertThat(updatedUser.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(updatedUser.getCertificationCode()).isEqualTo("aaaaa-aaaaa-aaaaaaaa");
        assertThat(updatedUser.getLastLoginAt()).isEqualTo(0L);
    }

    @Test
    public void User는_로그인_할_수_있다_로그인_후_마지막_로그인_시간이_변경된다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .certificationCode("aaaaa-aaaaa-aaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();
        //when
        ClockHolder clockHolder = new TestClockHolder(1234L);
        user = user.login(clockHolder);

        //then
        assertThat(user.getLastLoginAt()).isEqualTo(1234L);
    }

    @Test
    public void User는_인증코드로_계정의_상태를_활성화_할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .certificationCode("aaaaa-aaaaa-aaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();

        //when
        user = user.certificate("aaaaa-aaaaa-aaaaaaaa");

        //then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void User는_잘못된_인증코드로_계정을_활성화_시도하면_오류가_발생한다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .certificationCode("aaaaa-aaaaa-aaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();

        //when then
        assertThatThrownBy(() -> user.certificate("bbbb-aaaaa-aaaaaaaa"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

}