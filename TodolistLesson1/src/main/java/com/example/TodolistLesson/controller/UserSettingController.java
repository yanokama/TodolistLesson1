package com.example.TodolistLesson.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.UserService;
import com.example.TodolistLesson.form.UserSettingForm;


@Controller
@RequestMapping("/user")
public class UserSettingController {
	
	@Autowired 
	private ModelMapper modelMapper;

	@GetMapping("/setting")
	public String getUser(Model model,
		@AuthenticationPrincipal MUser user,
		@ModelAttribute("userForm") UserSettingForm form) {
		
		form = modelMapper.map(user, UserSettingForm.class);
		model.addAttribute("userForm", form);
		return "user/setting";
	}

}
