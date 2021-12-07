package com.example.TodolistLesson.domain.todo.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.TodolistLesson.domain.todo.service.TodoService;
import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.domain.user.model.Todo;
import com.example.TodolistLesson.repository.TodoMapper;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoMapper mapper;

	/**タイトル一覧取得*/	
	@Override
	public List<Title> getTitles(String userId) {
		return mapper.findTitle(userId);
	}
	
	/**タイトル一件取得*/	
	@Override
	public Title getTitleOne(Integer listId) {
		return mapper.findTitleOne(listId);
	}
	
	
	/**タイトル追加*/
	@Override
	public void insertTitle(Title title) {
		mapper.insertTitle(title);
	}
	
	/**タイトル更新*/
	@Override
	public void updateTitle(Title title) {
		mapper.updateTitle(title);
	}
	
	/**タイトル削除*/
	@Transactional
	@Override
	public void deleteTitle(Integer listId) {
		mapper.deleteTitle(listId);	
		mapper.deleteTodoByTitle(listId);
	}
	
	/**ユーザに紐つくタイトル、todo全削除*/
	@Transactional
	@Override
	public void deleteUsersTodo(String userId) {
		
		List<Title> titleList = getTitles(userId);
		List<Integer> listIds = new LinkedList<>();
		for(Title title : titleList) {
			listIds.add(title.getListId());
		}
		mapper.deleteUsersTitle(userId);		
		if (listIds.size() > 0) {
			mapper.deleteUsersTodo(listIds);
		}
	}
	
	/**todoリスト取得*/	
	@Override
	public List<Todo> getTodos(Integer todoId){
		return mapper.findTodo(todoId);
	}

	/**完了済みリスト取得*/	
	@Override
	public List<Todo> getCompletedTodos(Integer todoId){
		return mapper.findCompletedTodo(todoId);
	}
	
	/**todo追加*/
	public void insertTodo (Todo todo) {
		mapper.insertTodo(todo);
	}
	
	/**todo更新*/
	public void updateTodo (Todo todo){
		mapper.updateTodo(todo);
	}

	/**todo削除*/
	public void deleteTodo(Integer itemId) {
		mapper.deleteTodo(itemId);
	}
	
}
