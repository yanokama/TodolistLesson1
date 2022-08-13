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

import com.example.TodolistLesson.demo.annotation.WithMockCustomUser;
import com.example.TodolistLesson.demo.annotation.WithMockCustomUserSecrityContextFactory;
import com.example.TodolistLesson.domain.todo.service.impl.TodoServiceImpl;
import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.form.TitleForm;

@WebMvcTest(controllers = TitleListRestController.class,
includeFilters = @ComponentScan.Filter( 
	type = FilterType.ASSIGNABLE_TYPE,
	classes = { UserServiceImpl.class, TodoServiceImpl.class}
))
public class TitleListRestControllerTest {
	@Autowired
    MockMvc mvc;

    @MockBean
    UserServiceImpl userService;
    @MockBean
    TodoServiceImpl todoService;
	@MockBean 
	ModelMapper modelMapper;

	//テストデータ共通
	String userId = "testuser@xxx.co.jp";

	//各テストから呼び出される
    MockHttpServletRequestBuilder createRequest(String pass, String method, 
    		MultiValueMap<String, String> params ) {
    	URI uri = URI.create(pass);
    	return put(uri)
                .param(method, "")
                .params(params)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML);        
    }
    
	@Test
	@WithMockCustomUser
	public void タイトルを追加できる() throws Exception {
		//テストデータ準備
		String listName = "new todolist";
		Title newtitle = new Title();
		newtitle.setListId(null);
		newtitle.setUserId(userId);
		newtitle.setListName(listName);
	    MultiValueMap<String, String> reqPramOftitle =
	            new LinkedMultiValueMap<>();
	    /*listIdは自動採番なのでチェック対象外としている*/
		String expectedJson = "{"
				+ "\"result\":0, \"errors\":null,"
				+ "\"titles\":["
					+ "{\"userId\":\"testuser@xxx.co.jp\","
					+ " \"listName\":\"new todolist\"}]"
				+ "}";
		
		//モック化
		doNothing().when(todoService).insertTitle(newtitle);

		//exercise&verify
		mvc.perform(createRequest("/todo/title", "insert", reqPramOftitle))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
		verify(todoService, times(1)).insertTitle(newtitle);		
	}
	
	@Test
	@WithMockCustomUser
	public void タイトルを更新できる() throws Exception {
	    //テストデータ準備
		String listName = "updated todolist";
		
		TitleForm titleForm = new TitleForm();
		titleForm.setListId(1);
		titleForm.setUserId(userId);
		titleForm.setListName(listName);		

		Title title = new Title();
		title.setListId(1);
		title.setUserId(userId);
		title.setListName(listName);
		
	    MultiValueMap<String, String> reqPramOftitle =
	            new LinkedMultiValueMap<>() {{
	                add("listId", "1");
	                add("userId", userId);
	                add("listName", listName);
	            }};
		String expectedJson = "{"
				+ "\"result\":0, \"errors\":null,"
				+ "\"titles\":["
					+ "{\"listId\":1 ,"
					+ "\"userId\":\"testuser@xxx.co.jp\","
					+ " \"listName\":\"updated todolist\"}"
					+ "]"
				+ "}";
		
		//モック化
		when(modelMapper.map(titleForm, Title.class)).thenReturn(title);
		doNothing().when(todoService).updateTitle(title);
		
		//exercise&verify
		mvc.perform(createRequest("/todo/title", "update", reqPramOftitle))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
		verify(modelMapper, times(1)).map(titleForm, Title.class);
		verify(todoService, times(1)).updateTitle(title);
		
	}
	
	@Test
	@WithMockCustomUser
	public void タイトルを削除できる() throws Exception {
	    //テストデータ準備
		Integer deleteTitleNum = 1;
		
	    MultiValueMap<String, String> reqPramOftitle =
	            new LinkedMultiValueMap<>() {{
	                add("listId", "1");
	            }};
		String expectedJson = "{"
				+ "\"result\":0, \"errors\":null,"
				+ "\"titles\":["
					+ "{\"listId\":1 }"
					+ "]"
				+ "}";
		
		//モック化
		doNothing().when(todoService).deleteTitle(deleteTitleNum);
		
		//exercise&verify
		mvc.perform(createRequest("/todo/title", "delete", reqPramOftitle))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
		verify(todoService, times(1)).deleteTitle(deleteTitleNum);
	}	
	
}
