package com.example.TodolistLesson.rest;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.TodolistLesson.domain.todo.service.TodoService;
import com.example.TodolistLesson.domain.todo.service.impl.TodoServiceImpl;
import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.form.SignupForm;
import com.example.TodolistLesson.repository.TodoMapper;

@WebMvcTest(controllers = UserRestController.class,
includeFilters = @ComponentScan.Filter(
	type = FilterType.ASSIGNABLE_TYPE,
	classes = { UserServiceImpl.class, TodoServiceImpl.class, TodoMapper.class}
))
public class UserRestControllerTest {
	@Autowired
    MockMvc mvc;
    @MockBean
    UserServiceImpl userService;
    @MockBean
    TodoMapper todoMapper;

    //正常なデータ
    MultiValueMap<String, String> validData =
            new LinkedMultiValueMap<>() {{
                add("userId", "testuser1@co.jp");
                add("password", "password");
                add("appUserName", "test user1");
                add("gender", "1");
            }};
            
    //エラーになるデータ
    MultiValueMap<String, String> invalidData =
            new LinkedMultiValueMap<>() {{
                add("userId", "testuser1@co.jp");
                add("password", ""); 	//パスワードがブランク
                add("appUserName", "");	//名前がブランク
                add("gender", "1");
            }};            
    
    MockHttpServletRequestBuilder createRequest(
    		String pass, 
    		MultiValueMap<String, String> formData) {
    	URI uri = URI.create(pass);
    	return put(uri)
                .params(formData)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML);        
    }

	@Test
    @WithMockUser
	public void パスワードを更新できる() throws Exception {
		//setup
		final String expectedJson = "{\"result\":0,\"errors\":null}";
		MUser user = new MUser();
		user.setUserId("testuser1@co.jp");
		user.setAppUserName("test user1");
		user.setPassword("password");
		user.setGender(1);

		//モック化
		doNothing().when(userService).updateUserPass(user.getUserId(), user.getPassword());
		when(userService.loadUserByUsername(user.getUserId())).thenReturn(user);

		//exercise&verify
		mvc.perform(createRequest("/user/updatePass", validData))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
		verify(userService, times(1)).updateUserPass(user.getUserId(),"password");
		
	}
	
	@Test
    @WithMockUser
	public void パスワードが不正な場合９０が返却される() throws Exception {
		//setup
		final String expectedJson = "{\"result\":90}";

		//exercise&verify
		mvc.perform(createRequest("/user/updatePass", invalidData))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
	}	
    
    //名前を更新できる
	@Test
    @WithMockUser
	public void 名前を更新できる() throws Exception {
		//setup
		final String expectedJson = "{\"result\":0,\"errors\":null}";
		MUser user = new MUser();
		user.setUserId("testuser1@co.jp");
		user.setAppUserName("test user1");
		user.setPassword("password");
		user.setGender(1);

		//モック化
		doNothing().when(userService).updateUserName(user.getUserId(), user.getAppUserName());
		when(userService.loadUserByUsername(user.getUserId())).thenReturn(user);

		//exercise&verify
		mvc.perform(createRequest("/user/updateName", validData))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
		verify(userService, times(1)).updateUserName(user.getUserId(),user.getAppUserName());

	}
	
	@Test
    @WithMockUser
	public void 名前がブランクの場合９０が返却される() throws Exception {
		//setup
		final String expectedJson = "{\"result\":90}";

		//exercise&verify
		mvc.perform(createRequest("/user/updateName", invalidData))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
	}
}

