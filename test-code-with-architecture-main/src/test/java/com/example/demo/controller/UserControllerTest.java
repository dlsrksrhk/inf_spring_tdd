package com.example.demo.controller;

import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql"),
        @Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 사용자는_특정_유저의_정보를_전달받을_수_있다_단_주소지같은_개인정보는_제외된다() throws Exception {
        mockMvc.perform(get("/api/users/1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1000))
                .andExpect(jsonPath("$.email").value("test@naver.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.nickname").value("테스터"))
                .andExpect(jsonPath("$.address").doesNotExist());
    }


    @Test
    void 존재하지않는_사용자를_조회하면_404_응답을_받는다() throws Exception {
        mockMvc.perform(get("/api/users/777777"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 777777를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_할수있다() throws Exception {
        mockMvc.perform(
                        get("/api/users/1001/verify")
                                .queryParam("certificationCode", "BBBBB-aaaaa-aaaaaaaa")
                )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost:3000"));

        userRepository.findById(1001L)
                .ifPresent(userEntity -> {
                    assertEquals("ACTIVE", userEntity.getStatus().name());
                    assertEquals(1001L, userEntity.getId());
                });
    }

    @Test
    void 내정보를_조회할_수_있다() throws Exception {
        mockMvc.perform(get("/api/users/me").header("EMAIL", "test@naver.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1000))
                .andExpect(jsonPath("$.email").value("test@naver.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.nickname").value("테스터"))
                .andExpect(jsonPath("$.address").value("서울시 강남구"));
    }

    @Test
    void 내정보를_수정할_수_있다() throws Exception {
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .nickname("테스터_수정")
                .address("서울시 강북구")
                .build();

        mockMvc.perform(put("/api/users/me")
                        .header("EMAIL", "test@naver.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1000))
                .andExpect(jsonPath("$.email").value("test@naver.com"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.nickname").value("테스터_수정"))
                .andExpect(jsonPath("$.address").value("서울시 강북구"));
    }

}