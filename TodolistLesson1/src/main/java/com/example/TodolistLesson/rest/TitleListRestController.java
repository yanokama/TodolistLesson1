package com.example.TodolistLesson.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TodolistLesson.domain.todo.service.TodoService;
import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.form.TitleForm;

@RestController
@RequestMapping("/todo")
public class TitleListRestController {

	@Autowired 
	private TodoService todoService;
	@Autowired 
	private ModelMapper modelMapper;
	
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
	
	/**タイトル更新*/
	@PutMapping(value = "/title", params = "update")	
	public RestResultOfTitle updateTodo(@ModelAttribute TitleForm form, Model model) {
		Title title = modelMapper.map(form, Title.class);
		todoService.updateTitle(title);

		return new RestResultOfTitle(0, null, title);
	}
	
	/**タイトル削除*/	
	@PutMapping(value = "/title", params = "delete")
	public RestResultOfTitle deleteTitle(@ModelAttribute("listId") Integer listId, Model model) {

		todoService.deleteTitle(listId);
		Title deletedTitle = new Title();
		deletedTitle.setListId(listId);
		return new RestResultOfTitle(0, null, deletedTitle);
	}			

}
