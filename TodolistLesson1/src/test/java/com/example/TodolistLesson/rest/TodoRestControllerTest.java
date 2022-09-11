package com.example.TodolistLesson.rest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

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
import com.example.TodolistLesson.domain.todo.service.impl.TodoServiceImpl;
import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.domain.user.model.Todo;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.form.TodoForm;

@WebMvcTest(controllers = TodoRestController.class,
includeFilters = @ComponentScan.Filter( 
	type = FilterType.ASSIGNABLE_TYPE,
	classes = { UserServiceImpl.class, TodoServiceImpl.class, ModelMapper.class}
))
public class TodoRestControllerTest {
	@Autowired
    MockMvc mvc;

    @MockBean
    UserServiceImpl userService;
    @MockBean
    TodoServiceImpl todoService;
	@MockBean 
	ModelMapper modelMapper;
	
	//各テストから呼び出されるputリクエスト実行メソッド
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
	public void todoを追加できる() throws Exception {
    	//データ準備
		String expectedJson = "{"
				+ "\"result\":0, \"errors\":null,"
				+ "\"todos\":["
					+ "{\"listId\":11,"
					+ " \"priority\":3,"
					+ " \"importance\":3}]"
				+ "}";
		Todo newTodo = new Todo();
		newTodo.setListId(11);
		newTodo.setPriority(3);
		newTodo.setImportance(3);
	    MultiValueMap<String, String> reqParamOfTodo =
	            new LinkedMultiValueMap<>();
	    reqParamOfTodo.add("listId", "11");
	    
    	//モック化
		doNothing().when(todoService).insertTodo(newTodo);
   	
    	//テスト実行
		mvc.perform(createRequest("/todo/todo", "insert", reqParamOfTodo))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
		verify(todoService, times(1)).insertTodo(newTodo);				
    }
    
    @Test
	@WithMockCustomUser
	public void todoを更新できる() throws Exception {
    	//TODO：長すぎるのでデータ準備部を切り出す
    	//データ準備 
		String expectedJson = "{"
				+ "\"result\":0, \"errors\":null,"
				+ "\"todos\":["
					+ "{\"itemId\":111,"
					+ " \"listId\":222,"
					+ " \"itemName\":\"テストアイテム\","
					+ " \"doneFlg\":0,"
					+ " \"priority\":3,"
					+ " \"importance\":3}]"
				+ "}";
		
		String itemName = "テストアイテム";
		TodoForm form = new TodoForm();
		form.setItemId(111);
		form.setListId(222);
		form.setItemName(itemName);
		form.setDoneFlg(0);
		form.setPriority(3);
		form.setImportance(3);
		
		Todo todo = new Todo();
		todo.setItemId(111);
		todo.setListId(222);
		todo.setItemName(itemName);
		todo.setDoneFlg(0);
		todo.setPriority(3);
		todo.setImportance(3);
		
	    MultiValueMap<String, String> reqParamOfTodo =
	            new LinkedMultiValueMap<>();
	    reqParamOfTodo.add("ItemId", "111");
	    reqParamOfTodo.add("listId", "222");
	    reqParamOfTodo.add("ItemName", itemName);
	    reqParamOfTodo.add("doneFlg", "0");
	    reqParamOfTodo.add("priority", "3");
	    reqParamOfTodo.add("importance", "3");
	    
    	//モック化(ModelMapperもモック化)
		when(modelMapper.map(form, Todo.class)).thenReturn(todo);
		doNothing().when(todoService).updateTodo(todo);
   	
    	//テスト実行
		mvc.perform(createRequest("/todo/todo", "update", reqParamOfTodo))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
		verify(todoService, times(1)).updateTodo(todo);				
    }
    
	@Test
	@WithMockCustomUser
	public void todoを削除できる() throws Exception {
    	//TODO：長すぎるのでデータ準備部を切り出す
    	//データ準備 
		String expectedJson = "{"
				+ "\"result\":0, \"errors\":null,"
				+ "\"todos\":["
					+ "{\"itemId\":111,"
					+ " \"listId\":222,"
					+ " \"itemName\":\"テストアイテム\","
					+ " \"doneFlg\":0,"
					+ " \"priority\":3,"
					+ " \"importance\":3}]"
				+ "}";
		
		String itemName = "テストアイテム";
		TodoForm form = new TodoForm();
		form.setItemId(111);
		form.setListId(222);
		form.setItemName(itemName);
		form.setDoneFlg(0);
		form.setPriority(3);
		form.setImportance(3);
		
		Todo todo = new Todo();
		todo.setItemId(111);
		todo.setListId(222);
		todo.setItemName(itemName);
		todo.setDoneFlg(0);
		todo.setPriority(3);
		todo.setImportance(3);
		
	    MultiValueMap<String, String> reqParamOfTodo =
	            new LinkedMultiValueMap<>();
	    reqParamOfTodo.add("ItemId", "111");
	    reqParamOfTodo.add("listId", "222");
	    reqParamOfTodo.add("ItemName", itemName);
	    reqParamOfTodo.add("doneFlg", "0");
	    reqParamOfTodo.add("priority", "3");
	    reqParamOfTodo.add("importance", "3");
	    
    	//モック化(ModelMapperもモック化)
		doNothing().when(todoService).deleteTodo(form.getItemId());;
	    when(modelMapper.map(form, Todo.class)).thenReturn(todo);
		
    	//テスト実行
		mvc.perform(createRequest("/todo/todo", "delete", reqParamOfTodo))
		.andExpect(status().isOk())
		.andExpect(content().json(expectedJson));
		verify(todoService, times(1)).deleteTodo(form.getItemId());	
		
	}	    
}
