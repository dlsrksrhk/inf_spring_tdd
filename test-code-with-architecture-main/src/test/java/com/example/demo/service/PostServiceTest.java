package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.repository.PostEntity;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql"),
        @Sql(value = "/sql/post-service-test-data.sql"),
        @Sql(value = "/sql/delete-all-test-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Test
    void getById은_게시글을_가져온다() {
        Long id = 1000L;
        PostEntity result = postService.getById(id);
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void getById은_게시글을_찾지못하면_예외를던진다() {
        Long id = 7777L;
        assertThatThrownBy(() -> postService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void PostCreateDto를_이용하여_게시글을_생성할수있다() {
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .content("test content")
                .writerId(1000L)
                .build();

        PostEntity result = postService.create(postCreateDto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo(postCreateDto.getContent());
        assertThat(result.getWriter().getId()).isEqualTo(postCreateDto.getWriterId());
    }

    @Test
    void PostUpdateDto를_이용하여_게시글을_변경할수있다() {
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("update content")
                .build();

        PostEntity result = postService.update(1000L, postUpdateDto);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo(postUpdateDto.getContent());
    }

}