package com.example.TodolistLesson.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.form.SignupForm;
import com.example.TodolistLesson.rest.SignupRestController;

@WebMvcTest(controllers = SignupRestController.class,
includeFilters = @ComponentScan.Filter(
	type = FilterType.ASSIGNABLE_TYPE,
	classes = { UserServiceImpl.class, ModelMapper.class}
))
@WebAppConfiguration
public class signupRestControllerTest {
	@Autowired
    MockMvc mvc;
    @MockBean
    UserServiceImpl userService;
    @MockBean
	ModelMapper modelMapper;

    MultiValueMap<String, String> validData =
            new LinkedMultiValueMap<>() {{
                add("userId", "testuser1@co.jp");
                add("password", "password");
                add("appUserName", "test user1");
                add("gender", "1");
            }};
            
    MockHttpServletRequestBuilder createRequest(MultiValueMap<String, String> formData) {
        return post("/signup/signup")
                .params(formData)
                .with(csrf())     
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML);        
    }
	
	@Test
	public void 新規登録できる() throws Exception {
		//setup
		final String expectedJson = "{\"result\":0,\"errors\":null}";
		MUser newUser = new MUser();
		newUser.setUserId("testuser1@co.jp");
		newUser.setAppUserName("test user1");
		newUser.setPassword("password");
		newUser.setGender(1);
		SignupForm form = new SignupForm();
		form.setUserId("testuser1@co.jp");
		form.setAppUserName("test user1");
		form.setPassword("password");
		form.setGender(1);
		doNothing().when(userService).signup(newUser);
		when(modelMapper.map(form, MUser.class)).thenReturn(newUser);

		//exercise&verify
		mvc.perform(createRequest(validData))
        .andExpect(status().isOk())
		.andExpect(content().json(expectedJson));	
		verify(userService, times(1)).signup(newUser);
	}
	
	@Test
	public void 入力に誤りがある場合はエラー() throws Exception {
		//setup
		//本当はバリデーションをモック化したいが、いい手段が思い浮かばなかった
	    MultiValueMap<String, String> invalidData =
	            new LinkedMultiValueMap<>() {{
	                add("userId", "");
	                add("password", "password");
	                add("appUserName", "test user1");
	                add("gender", "1");
	            }};	  

	    //リターンコードのみをチェック対象とし、メッセージはチェックしない
		final String expectedJson = "{\"result\":90}";
	    //exercise&verify
	    mvc.perform(createRequest(invalidData))
	    .andExpect(status().isOk())
	    .andExpect(content().json(expectedJson));
	}
	
	@Test
	public void すでに存在するIDはエラー() throws Exception{
		//setup
		when(userService.getUserOne("testuser1@co.jp")).thenReturn(1);
		final String expectedJson = "{\"result\":90, \"errors\":{\"userId\":\"id already exists\"}}";	
	    //exercise&verify		
		mvc.perform(createRequest(validData))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
	}
	
	//例外発生時のテストは別途。
}
