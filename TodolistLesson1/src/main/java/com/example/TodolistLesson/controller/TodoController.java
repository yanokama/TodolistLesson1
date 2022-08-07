package com.example.TodolistLesson.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.TodolistLesson.comparator.TodoFormComparator;
import com.example.TodolistLesson.domain.todo.service.TodoService;
import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.domain.user.model.Todo;
import com.example.TodolistLesson.form.TitleForm;
import com.example.TodolistLesson.form.TodoForm;
import com.example.TodolistLesson.rest.CommonRestResult;

@Controller
@RequestMapping("/todo")
public class TodoController {
	
	@Autowired 
	private TodoService todoService;
	@Autowired 
	private ModelMapper modelMapper;	
	
	@GetMapping("/todo/{listId:.+}")
	public String getTodoList( Model model,
			@PathVariable("listId") Integer listId,
			@AuthenticationPrincipal MUser user) {
		
		Title title = todoService.getTitleOne(listId);
		//リストの所有社とユーザidが一致しないときは404を返す
		if (!(title.getUserId().equals(user.getUserId()))) {
			return "redirect:/error/404";
		}
		TitleForm titleForm = modelMapper.map(title, TitleForm.class);
		model.addAttribute("titleForm", titleForm);

		//todo取得
		List<Todo> todoList = todoService.getTodos(listId);
		List<TodoForm> todoFormList = new ArrayList<TodoForm>();
		TodoFormComparator todoFormComparator = new TodoFormComparator(); 
		for(Todo todo : todoList) {
			todoFormList.add( modelMapper.map(todo, TodoForm.class));
		}
		Collections.sort(todoFormList, todoFormComparator);	
		model.addAttribute("todoFormList", todoFormList);
		
		//完了済み取得
		List<Todo> doneList = todoService.getCompletedTodos(listId);
		List<TodoForm> doneFormList = new ArrayList<TodoForm>();
		for(Todo done : doneList) {
			doneFormList.add( modelMapper.map(done, TodoForm.class));
		}
		Collections.sort(todoFormList, todoFormComparator);	
		model.addAttribute("doneFormList", doneFormList);		
		
		return "todo/todo";
		
	}

	/**追加*/
	@PostMapping(value = "/todo" , params = "insert")
	public String insertTodo(@RequestParam("listId") Integer listId,
			Model model) {

		//todo登録
		Todo todo = new Todo();
		todo.setListId(listId);
		todo.setPriority(3);
		todo.setImportance(3);
		todoService.insertTodo(todo);

		return "redirect:/todo/todo/"+ listId;
	}	
	
	/**todo削除*/
	@PostMapping(value = "/todo" , params = "delete")
	public String delete(@ModelAttribute TodoForm form, Model model) {
		
		todoService.deleteTodo(form.getItemId());

		return "redirect:/todo/todo/" + form.getListId();
	}

	/**todo更新*/
	@PostMapping(value = "/todo/update")
	@ResponseBody
	public CommonRestResult updateTodo(@ModelAttribute TodoForm form, Model model) {

		//todo更新
		Todo todo = modelMapper.map(form, Todo.class);
		todoService.updateTodo(todo);

		//結果の返却
		return new CommonRestResult(0, null);
	}	

}
