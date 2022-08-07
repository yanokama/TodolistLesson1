package com.example.TodolistLesson.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TodolistLesson.domain.todo.service.TodoService;
import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.model.Title;

@RestController
@RequestMapping("/todo")
public class TitleListRestController {

	@Autowired 
	private TodoService todoService;
	
	/**タイトル追加*/
	@PutMapping(value = "/title", params = "insert")
	public RestResultOfTitle insertTitle(Model model,
			@AuthenticationPrincipal MUser user ) {
		Title title = new Title();		
		title.setUserId(user.getUserId());
		title.setListName("new todolist");
		todoService.insertTitle(title);
		
		return new RestResultOfTitle(0, null, title);	
	}

}
