package com.example.TodolistLesson.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.TodolistLesson.application.service.UserApplicationService;
import com.example.TodolistLesson.controller.SignupController;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

@WebMvcTest(controllers = SignupController.class,
	includeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = {UserApplicationService.class, UserServiceImpl.class}
	))
public class SignUpControllerTest {
    @Autowired
    MockMvc mvc;

    // UserApplicationServiceをMockitoでモック化する
    @MockBean
    UserApplicationService userService;
    @MockBean
    UserServiceImpl userServiceImpl;

    //リクエストが着たら画面表示
    @Test
    void signUpにアクセスするとサインアップ画面が返される() throws Exception {
        mvc.perform(get("/signup/signup").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("signup/signup"));
    }
    //signupできる
    //signupできない
}
