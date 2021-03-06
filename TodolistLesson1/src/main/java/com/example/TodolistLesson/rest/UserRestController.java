package com.example.TodolistLesson.rest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TodolistLesson.domain.todo.service.TodoService;
import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.UserService;
import com.example.TodolistLesson.form.UserDeleteForm;
import com.example.TodolistLesson.form.UserSettingForm;
import com.example.TodolistLesson.form.ValidGroup1;
import com.example.TodolistLesson.form.ValidGroup2;


@RestController
@RequestMapping("/user")
public class UserRestController {

	@Autowired
	private UserService userService;
	@Autowired
	private TodoService todoService;

	@Autowired
	private MessageSource messageSource;

	
	/**?????????????????????*/
	@PutMapping("/updatePass")
	public RestResult updatePass(
			@ModelAttribute("userForm") @Validated(ValidGroup2.class) UserSettingForm form,
			BindingResult bindingResult,
			Model model, Locale locale) {

		if(bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for(FieldError error:bindingResult.getFieldErrors()) {
				String message = messageSource.getMessage(error, locale);
				errors.put(error.getField(), message);
			}
			return new RestResult(90, errors);
		}
		userService.updateUserPass(form.getUserId(),
				form.getPassword());

		//?????????????????????????????????????????????
		UserDetails user = userService.loadUserByUsername(form.getUserId());
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken(
				user, user.getPassword(), user.getAuthorities()));

		return new RestResult(0, null);
	}

	/**?????????????????????*/
	@PutMapping("/updateName")
	public RestResult updateUsername(
			@ModelAttribute("userForm") @Validated(ValidGroup1.class) UserSettingForm form,
			BindingResult bindingResult,
			Model model, Locale locale) {
		if(bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for(FieldError error:bindingResult.getFieldErrors()) {
				String message = messageSource.getMessage(error, locale);
				errors.put(error.getField(), message);
			}
			return new RestResult(90, errors);
		}

		userService.updateUserName(form.getUserId(),
				form.getAppUserName());

		//?????????????????????????????????????????????
		UserDetails user = userService.loadUserByUsername(form.getUserId());
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken(
				user, user.getPassword(), user.getAuthorities()));

		return new RestResult(0, null);
	}

	@DeleteMapping("/delete")
	@Transactional
	public int deleteUser(@AuthenticationPrincipal MUser user,
		HttpServletRequest request, UserDeleteForm form) {
		//??????????????????
		todoService.deleteUsersTodo(user.getUserId());
		userService.deleteUser(user.getUserId());
		//?????????????????????????????????????????????
		try {
			request.logout();
		} catch(ServletException e ) {
			throw new RuntimeException("delete and logout failure", e);
		}
		return 0;
	}

	/**???????????????????????????????????????*/
	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e, Model model) {

		//?????????????????????
		model.addAttribute("error","");

		//??????????????????Model?????????
		model.addAttribute("message","UserRestController ??????????????????????????????");

		//HTTP????????????????????????500??????Model?????????
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

		return "error";
	}

	/** ???????????????????????? */
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {

		//?????????????????????
		model.addAttribute("error","");

		//??????????????????Model?????????
		model.addAttribute("message","UserRestController ??????????????????????????????");

		//HTTP????????????????????????500??????Model?????????
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

		return "error";
	}
}
