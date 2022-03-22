package com.example.TodolistLesson.rest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.UserService;
import com.example.TodolistLesson.form.GroupOrder;
import com.example.TodolistLesson.form.SignupForm;

@RestController
@RequestMapping("/signup")
public class SignupRestController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private MessageSource messageSource;	
	/**ユーザーを登録*/
	@PostMapping("/signup")
	public RestResult postSignup(@Validated(GroupOrder.class) SignupForm form,
			BindingResult bindingResult, Locale locale) {
		//入力チェック結果
		if(bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for(FieldError error:bindingResult.getFieldErrors()) {
				String message = messageSource.getMessage(error, locale);
				errors.put(error.getField(), message);
			}
			return new RestResult(90, errors);
		}
		
		//既存のIDとの重複チェック
		int userCount = userService.getUserOne(form.getUserId());
		if(userCount != 0) {
			Map<String, String> errors = new HashMap<>();			
			String message = messageSource.getMessage("duplicateId",null, locale);			
			errors.put("userId", message);
			return new RestResult(90, errors);
		}
		
		MUser user =modelMapper.map(form, MUser.class);
		userService.signup(user);
		
		return new RestResult(0, null);
	}
}
