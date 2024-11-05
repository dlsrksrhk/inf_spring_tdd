package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostServiceTest {
    private PostService postService;

    @BeforeEach
    void setUp() {
        FakePostRepository postRepository = new FakePostRepository();
        FakeUserRepository userRepository = new FakeUserRepository();

        this.postService = PostService.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(new TestClockHolder(1698765432L))
                .build();

        User writer1 = userRepository.save(User.builder()
                .id(1000L)
                .email("test@naver.com")
                .nickname("테스터")
                .address("서울시 강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaa-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build());

        User writer2 = userRepository.save(User.builder()
                .id(1001L)
                .email("test2@naver.com")
                .nickname("테스터2")
                .address("서울시 강남구")
                .status(UserStatus.PENDING)
                .certificationCode("BBBBB-aaaaa-aaaaaaaa")
                .lastLoginAt(0L)
                .build());

        postRepository.save(Post.builder()
                .id(1000L)
                .content("First post content")
                .createdAt(1698765432L)
                .modifiedAt(1698765432L)
                .writer(writer1)
                .build());

        postRepository.save(Post.builder()
                .id(1001L)
                .content("Second post content")
                .createdAt(1698765442L)
                .modifiedAt(1698765442L)
                .writer(writer2)
                .build());

        postRepository.save(Post.builder()
                .id(1002L)
                .content("Third post content")
                .createdAt(1698765452L)
                .modifiedAt(1698765452L)
                .writer(writer1)
                .build());

        postRepository.save(Post.builder()
                .id(1003L)
                .content("Fourth post content")
                .createdAt(1698765462L)
                .modifiedAt(1698765462L)
                .writer(writer2)
                .build());

    }

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
        assertThat(result.getCreatedAt()).isEqualTo(1698765432L);
        assertThat(result.getModifiedAt()).isEqualTo(1698765432L);
    }

}