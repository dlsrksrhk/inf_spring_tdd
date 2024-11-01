package com.example.demo.repository;

import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = true)
@SqlGroup({
        @Sql(value = "/sql/user-repo-test-data.sql"),
        @Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus_테스트() {
        Optional<UserEntity> finedEntity = userRepository.findByIdAndStatus(1000L, UserStatus.ACTIVE);
        assertThat(finedEntity.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_조회_안되면_Optional_empty_테스트() {
        Optional<UserEntity> finedEntity = userRepository.findByIdAndStatus(1000L, UserStatus.PENDING);
        assertThat(finedEntity.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_테스트() {
        Optional<UserEntity> finedEntity = userRepository.findByEmailAndStatus("test@naver.com", UserStatus.ACTIVE);
        assertThat(finedEntity.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_조회_안되면_Optional_empty_테스트() {
        Optional<UserEntity> finedEntity = userRepository.findByEmailAndStatus("test@naver.com", UserStatus.PENDING);
        assertThat(finedEntity.isEmpty()).isTrue();
    }
}