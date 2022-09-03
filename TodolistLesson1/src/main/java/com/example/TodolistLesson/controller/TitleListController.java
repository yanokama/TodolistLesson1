package com.example.TodolistLesson.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.TodolistLesson.comparator.TitleFormComparator;
import com.example.TodolistLesson.domain.todo.service.TodoService;
import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.form.TitleForm;

@Controller
@RequestMapping("/todo")
public class TitleListController {

	@Autowired 
	private TodoService todoService;
	
	@Autowired 
	private ModelMapper modelMapper;
	
	@GetMapping("/title")
	public String getTitleList(Model model,
			@AuthenticationPrincipal MUser user) {
		
		//ユーザーのタイトル一覧取得
		List<Title> titleList = todoService.getTitles(user.getUserId());
		
		List<TitleForm> titleFormList = new LinkedList<TitleForm>();
		for(Title title : titleList) {
			titleFormList.add( modelMapper.map(title, TitleForm.class));
		}
		
		//Modelに登録
		Collections.sort(titleFormList, new TitleFormComparator());	
		model.addAttribute("TitleForms", titleFormList);
		
		return "todo/title";
	}		
}
