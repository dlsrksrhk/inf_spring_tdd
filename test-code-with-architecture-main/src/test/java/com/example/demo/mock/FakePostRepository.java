package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakePostRepository implements PostRepository {
    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<Post> datas = new ArrayList<>();

    @Override
    public Optional<Post> findById(long id) {
        return datas.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst();
    }

    @Override
    public Post save(Post post) {
        if (post == null) {
            return null;
        }

        if (post.getId() == null || post.getId() == 0) {
            Post newPost = Post.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .writer(post.getWriter())
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .build();
            datas.add(newPost);
            return newPost;
        }

        datas.removeIf(p -> p.getId().equals(post.getId()));
        datas.add(post);
        return post;
    }
}