package com.example.demo.user.service.port;

import com.example.demo.user.domain.UserEntity;
import com.example.demo.user.domain.UserStatus;

import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByIdAndStatus(long id, UserStatus userStatus);

    Optional<UserEntity> findByEmailAndStatus(String email, UserStatus userStatus);

    UserEntity save(UserEntity userEntity);

    Optional<UserEntity> findById(long id);
}
