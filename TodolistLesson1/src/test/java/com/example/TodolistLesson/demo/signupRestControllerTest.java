package com.example.TodolistLesson.demo;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.form.SignupForm;
import com.example.TodolistLesson.repository.UserMapper;
import com.example.TodolistLesson.rest.RestResult;
import com.example.TodolistLesson.rest.SignupRestController;

@WebMvcTest(controllers = SignupRestController.class,
includeFilters = @ComponentScan.Filter(
	type = FilterType.ASSIGNABLE_TYPE,
	classes = { UserServiceImpl.class, ModelMapper.class}
))

public class signupRestControllerTest {
    @Autowired
    MockMvc mvc;
    
    @MockBean
    UserServiceImpl userService;
    @MockBean
	UserMapper userMapper;
    @MockBean
	ModelMapper modelMapper;

    final MultiValueMap<String, String> validData =
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
                //.accept(MediaType.TEXT_HTML);        
                .accept(MediaType.APPLICATION_JSON);        

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
	
//バリデーションエラー
	
//重複エラー

//データベース例外発生
}
