package com.example.TodolistLesson.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.form.UserSettingForm;



@WebMvcTest(controllers = UserSettingController.class,
includeFilters = @ComponentScan.Filter(
	type = FilterType.ASSIGNABLE_TYPE,
	classes = { ModelMapper.class, UserServiceImpl.class}
))
public class UserSettingControllerTest {
	@Autowired
    MockMvc mvc;
	@MockBean
	UserServiceImpl userServiceImpl;
	@MockBean
	ModelMapper modelMapper;
	
	UserSettingForm userSettingForm;

	@BeforeEach
	void init() {
		userSettingForm = new UserSettingForm();
	}
	
	@Test
    @WithMockUser
    void settingにアクセスするとユーザー設定画面が返される() throws Exception {
		//setup
		MUser user = null;
		userSettingForm.setUserId("testUser1@xxx.co.jp");
		userSettingForm.setPassword("password");
		when(modelMapper.map(user, UserSettingForm.class)).thenReturn(userSettingForm);
		//exercise & verify
        mvc.perform(get("/user/setting").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("user/setting"));
    }
	
	@Test
    void 認証が無い場合はアクセス不可() throws Exception {
		//setup
		//exercise & verify
        mvc.perform(get("/user/setting").accept(MediaType.TEXT_HTML))
                .andExpect(status().isFound()); //ログイン画面にリダイレクトされる
    }

}
