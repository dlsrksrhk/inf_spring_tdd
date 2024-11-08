package com.example.demo.mock;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.common.service.port.UuidHolder;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.AuthenticationService;
import com.example.demo.user.controller.port.UserCreateService;
import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.controller.port.UserUpdateService;
import com.example.demo.user.service.CertificationServiceImpl;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;

public class TestContainer {
    public final MailSender mailSender;
    public final UserRepository userRepository;
    public final PostRepository postRepository;
    public final UserCreateService userCreateService;
    public final UserReadService userReadService;
    public final UserUpdateService userUpdateService;
    public final AuthenticationService authenticationService;
    public final PostService postService;
    public final CertificationServiceImpl certificationService;
    public final UserController userController;
    public final UserCreateController userCreateController;

    private TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {
        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.certificationService = new CertificationServiceImpl(mailSender);
        UserServiceImpl userService = UserServiceImpl.builder()
                .certificationService(certificationService)
                .clockHolder(clockHolder)
                .userRepository(userRepository)
                .uuidHolder(uuidHolder)
                .build();
        this.userCreateService = userService;
        this.userReadService = userService;
        this.userUpdateService = userService;
        this.authenticationService = userService;
        this.postService = PostServiceImpl.builder()
                .postRepository(postRepository)
                .userRepository(userRepository)
                .clockHolder(new TestClockHolder(1698765432L))
                .build();

        this.userController = UserController.builder()
                .userReadService(userReadService)
                .authenticationService(authenticationService)
                .userUpdateService(userUpdateService)
                .userCreateService(userCreateService)
                .build();

        this.userCreateController = UserCreateController.builder()
                .userCreateService(userCreateService)
                .build();
    }

    public static TestContainer create(ClockHolder clockHolder, UuidHolder uuidHolder) {
        return new TestContainer(clockHolder, uuidHolder);
    }
}
