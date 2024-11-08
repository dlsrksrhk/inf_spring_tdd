package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.user.controller.port.CertificationService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationServiceTest {
    @Test
    public void 알맞은_인증메일_제목_내용을_만들고_MailSender에게_전송을_요청한다() {
        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationServiceImpl(fakeMailSender);

        //when
        certificationService.send("test2@naver.com", 1001L, "BBBBB-aaaaa-aaaaaaaa");

        //then
        assertThat(fakeMailSender.email).isEqualTo("test2@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1001/verify?certificationCode=BBBBB-aaaaa-aaaaaaaa");
    }

}