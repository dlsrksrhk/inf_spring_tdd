package com.example.demo.user.infrastructure;

import com.example.demo.user.service.port.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {
    private final JavaMailSender mailSender;

    @Override
    public void send(String email, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tkfkatoa1@naver.com");
        message.setTo(email);
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }
}
