package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    @Test
    void testSendMail(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tkfkatoa1@naver.com");
        message.setTo("tkfkatoa1@naver.com");
        message.setSubject("Please certify your email address");
        message.setText("Please click the following link to certify your email address: ");
        mailSender.send(message);
    }

}