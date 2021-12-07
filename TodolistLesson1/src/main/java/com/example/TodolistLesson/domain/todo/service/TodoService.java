package com.example.TodolistLesson.domain.todo.service;

import java.util.List;

import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.domain.user.model.Todo;

public interface TodoService {

	/**タイトル取得*/
	public List<Title> getTitles(String userId);	
	
	/**タイトル一件取得*/
	Title getTitleOne(Integer listId);	
	
	/**タイトル追加*/
	public void insertTitle(Title title);

	/**タイトル更新*/
	public void updateTitle(Title title);

	/**タイトル削除*/
	public void deleteTitle(Integer listId);
	
	/**ユーザーに紐つくタイトル・todo全削除*/
	public void deleteUsersTodo(String userId);

	/**todo取得*/
	public List<Todo> getTodos(Integer todoId);	

	/**todo取得*/
	public List<Todo> getCompletedTodos(Integer todoId);	
	
	/**todo追加*/
	public void insertTodo (Todo todo);

	/**todo更新*/
	public void updateTodo (Todo todo);

	/**todo削除*/
	public void deleteTodo(Integer itemId);
}
