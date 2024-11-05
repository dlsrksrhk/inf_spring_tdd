package com.example.demo.medium.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Post result = postService.getById(id);
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void getById은_게시글을_찾지못하면_예외를던진다() {
        Long id = 7777L;
        assertThatThrownBy(() -> postService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void PostCreate를_이용하여_게시글을_생성할수있다() {
        PostCreate postCreate = PostCreate.builder()
                .content("test content")
                .writerId(1000L)
                .build();

        Post result = postService.create(postCreate);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo(postCreate.getContent());
        assertThat(result.getWriter().getId()).isEqualTo(postCreate.getWriterId());
    }

    @Test
    void PostUpdate를_이용하여_게시글을_변경할수있다() {
        PostUpdate postUpdate = PostUpdate.builder()
                .content("update content")
                .build();

        Post result = postService.update(1000L, postUpdate);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo(postUpdate.getContent());
    }

}