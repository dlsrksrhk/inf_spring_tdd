package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {
    @Test
    void Post는_PostCreate로부터_생성될_수_있다() {
        //given
        User writer = User.builder()
                .email("test@naver.com")
                .nickname("test")
                .address("강남구")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaa-aaaaa-aaaaaaaa")
                .build();

        PostCreate postCreate = PostCreate.builder()
                .writerId(1000L)
                .content("content")
                .build();
        //when
        Post post = Post.from(writer, postCreate, new TestClockHolder(1698765432L));

        //then
        assertThat(post.getWriter().getEmail()).isEqualTo("test@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("test");
        assertThat(post.getWriter().getAddress()).isEqualTo("강남구");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaa-aaaaa-aaaaaaaa");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.getCreatedAt()).isEqualTo(1698765432L);
    }

    @Test
    void Post는_PostUpdate로부터_내용이_수정될_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("new content")
                .build();
        //when
        Post post = Post.builder()
                .content("old content")
                .build();

        Post updatedPost = post.update(postUpdate, new TestClockHolder(1698765432L));

        //then
        assertThat(updatedPost.getContent()).isEqualTo("new content");
        assertThat(updatedPost.getModifiedAt()).isEqualTo(1698765432L);
    }

}