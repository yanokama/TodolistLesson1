package com.example.TodolistLesson.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.TodolistLesson.demo.annotation.TestWithUser;
import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.form.UserSettingForm;
import com.example.TodolistLesson.repository.UserMapper;



@WebMvcTest(controllers = UserSettingController.class,
includeFilters = @ComponentScan.Filter(
	type = FilterType.ASSIGNABLE_TYPE,
	classes = { ModelMapper.class, UserServiceImpl.class, UserSettingForm.class, UserMapper.class,}
))
public class UserSettingControllerTest {
	@Autowired
    MockMvc mvc;
	@MockBean
	UserMapper userMapper;
	@Autowired
	UserServiceImpl userServiceImpl;
	@MockBean
	ModelMapper modelMapper;
	
	private UserDetails testUser;
	@BeforeEach
	void init() {
		MUser user = new MUser();
		user.setUserId("testuser1@xxx.co.jp");
		user.setPassword("password");
		user.setAppUserName("testuser");
		user.setGender(1);
		testUser = user;
		userServiceImpl = Mockito.mock(UserServiceImpl.class);
		when(userMapper.findLoginUser("testuser1@xxx.co.jp"))
		.thenReturn(user);
    	when(userServiceImpl.loadUserByUsername("testuser1@xxx.co.jp"))
		.thenReturn(testUser);
	}
	
	@TestWithUser
    void settingにアクセスするとユーザー設定画面が返される() throws Exception {

		//exercise
        mvc.perform(get("/user/setting").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("user/setting"));
    }

}
