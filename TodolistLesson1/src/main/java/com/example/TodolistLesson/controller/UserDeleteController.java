package com.example.TodolistLesson.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class UserDeleteController {
	
	@GetMapping("/delete")
	public String getUser(Model model) {
		return "user/delete";
	}
	
}
