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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.TodolistLesson.demo.annotation.TestWithUser;
import com.example.TodolistLesson.demo.annotation.WithMockCustomUser;
import com.example.TodolistLesson.demo.annotation.WithMockCustomUserSecrityContextFactory;
import com.example.TodolistLesson.domain.todo.service.impl.TodoServiceImpl;
import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.repository.TodoMapper;
import com.example.TodolistLesson.repository.UserMapper;

@WebMvcTest(controllers = TitleListRestController.class,
includeFilters = @ComponentScan.Filter( 
	type = FilterType.ASSIGNABLE_TYPE,
	classes = { UserServiceImpl.class, TodoServiceImpl.class,
				WithMockCustomUser.class, WithMockCustomUserSecrityContextFactory.class}
))
public class TitleListRestControllerTest {
	@Autowired
    MockMvc mvc;
    @MockBean
    UserServiceImpl userService;
    @MockBean
    TodoServiceImpl todoService;

	/*リクエスト作成メソッド*/
    MockHttpServletRequestBuilder createRequest(String pass ) {
    	URI uri = URI.create(pass);
    	return put(uri)
                .param("insert", "")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML);        
    }
    
	@Test
	@WithMockCustomUser
	public void タイトルを追加できる() throws Exception {

		String expectedJson = "{"
				+ "\"result\":0, \"errors\":null,"
				+ "\"titles\":[{\"userId\":\"testuser@xxx.co.jp\", \"listName\":\"new todolist\"}]"
				+ "}";
		//モック化
		doNothing().when(todoService).insertTitle(null);

		//exercise&verify
		mvc.perform(createRequest("/todo/title"))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
	}
}
