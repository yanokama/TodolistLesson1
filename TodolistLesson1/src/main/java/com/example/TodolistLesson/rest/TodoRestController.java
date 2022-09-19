package com.example.TodolistLesson.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.TodolistLesson.domain.todo.service.TodoService;
import com.example.TodolistLesson.domain.user.model.Todo;
import com.example.TodolistLesson.form.TodoForm;

@RestController
@RequestMapping("/todo")
public class TodoRestController {

	@Autowired 
	private TodoService todoService;
	@Autowired 
	private ModelMapper modelMapper;		
	
	/**todo追加*/
	@PutMapping(value = "/todo" , params = "insert")
	public RestResultOfTodo insertTodo(@RequestParam("listId") Integer listId,
			Model model) {

		Todo todo = new Todo();
		todo.setListId(listId);
		todo.setPriority(3);
		todo.setImportance(3);
		todoService.insertTodo(todo);

		return new RestResultOfTodo(0, null, todo);
	}
	
	/**todo更新*/
	@PutMapping(value = "/todo", params = "update")
	public RestResultOfTodo updateTodo(@ModelAttribute TodoForm form, Model model) {

		Todo todo = modelMapper.map(form, Todo.class);
		todoService.updateTodo(todo);
		
		return new RestResultOfTodo(0, null, todo);
	}
	
	/**todo削除*/
	@PutMapping(value = "/todo" , params = "delete")
	public RestResultOfTodo delete(@ModelAttribute TodoForm form, Model model) {

		todoService.deleteTodo(form.getItemId());
		Todo todo = modelMapper.map(form, Todo.class);

		return new RestResultOfTodo(0, null, todo);
	}
}
