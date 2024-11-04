package com.example.demo.user.service.port;

import com.example.demo.user.domain.UserEntity;
import com.example.demo.user.domain.UserStatus;

import java.util.Optional;

public interface MailSender {
    void send(String email, String title, String content);
}
