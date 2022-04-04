package com.example.TodolistLesson.demo;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.form.SignupForm;
import com.example.TodolistLesson.repository.UserMapper;
import com.example.TodolistLesson.rest.SignupRestController;

@WebMvcTest(controllers = SignupRestController.class,
includeFilters = @ComponentScan.Filter(
	type = FilterType.ASSIGNABLE_TYPE,
	classes = { UserServiceImpl.class, ModelMapper.class}
))

public class signupRestControllerTest {
    @Autowired
    MockMvc mvc;
//    @Autowired
//    SignupRestController target;
//    @Autowired
//    private HandlerExceptionResolver handlerExceptionResolver;
    
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
		mvc.perform(createRequest(validData))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
	}
	
//	@Test
//	public void 登録失敗時はエラー画面に遷移() throws Exception{
//		this.mvc = MockMvcBuilders.standaloneSetup(target)
//				.setControllerAdvice(target)
//				.build();
//		//setup
//		MUser newUser = new MUser();
//		newUser.setUserId("testuser1@co.jp");
//		newUser.setAppUserName("test user1");
//		newUser.setPassword("password");
//		newUser.setGender(1);
//		doThrow(DataAccessException.class)
//		.when(userMapper).insertOne(newUser);
//		
//		//exercise & verify
//		mvc.perform(createRequest(validData))
//		.andExpect(status().isOk())
//        .andExpect(view().name("error"));
//	}
}
