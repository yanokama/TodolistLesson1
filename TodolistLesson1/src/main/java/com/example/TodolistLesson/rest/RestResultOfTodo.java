package com.example.TodolistLesson.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.domain.user.model.Todo;

import lombok.Data;

@Data
public class RestResultOfTodo extends CommonRestResult {
	private List<Todo> todos;
	
	/*todoなし*/
	public RestResultOfTodo(
			int result,
			Map<String, String> errors
			) {
		super(result, errors);		
	}
	
	/*todo1件*/
	public RestResultOfTodo(
			int result,
			Map<String, String> errors,
			Todo todo
			) {
		super(result, errors);
		todos = new ArrayList<>(){{add(todo);}};
	}
}
