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

//	/**ユーザーを登録*/
//	@PostMapping("/signup")
//	public RestResult postSignup(@Validated(GroupOrder.class) SignupForm form,
//			BindingResult bindingResult, Locale locale) {
//		//入力チェック結果
//		if(bindingResult.hasErrors()) {
//			Map<String, String> errors = new HashMap<>();
//			for(FieldError error:bindingResult.getFieldErrors()) {
//				String message = messageSource.getMessage(error, locale);
//				errors.put(error.getField(), message);
//			}
//			return new RestResult(90, errors);
//		}
//
//		//既存のIDとの重複チェック
//		int userCount = userService.getUserOne(form.getUserId());
//		if(userCount != 0) {
//			Map<String, String> errors = new HashMap<>();
//			String message = messageSource.getMessage("duplicateId",null, locale);
//			errors.put("userId", message);
//			return new RestResult(90, errors);
//		}
//
//		MUser user =modelMapper.map(form, MUser.class);
//		userService.signup(user);
//
//		return new RestResult(0, null);
//	}

	/**パスワード更新*/
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

		//更新成功したら、認証情報も変更
		UserDetails user = userService.loadUserByUsername(form.getUserId());
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken(
				user, user.getPassword(), user.getAuthorities()));

		return new RestResult(0, null);
	}

	/**ユーザー名更新*/
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

		//更新成功したら、認証情報も変更
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
		//ユーザー削除
		todoService.deleteUsersTodo(user.getUserId());
		userService.deleteUser(user.getUserId());
		//ログアウト処理で認証情報を破棄
		try {
			request.logout();
		} catch(ServletException e ) {
			throw new RuntimeException("delete and logout failure", e);
		}
		return 0;
	}

	/**データベース関連の例外処理*/
	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e, Model model) {

		//空文字をセット
		model.addAttribute("error","");

		//メッセージをModelに登録
		model.addAttribute("message","UserRestController で例外が発生しました");

		//HTTPのエラーコード（500）をModelに登録
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

		return "error";
	}

	/** その他の例外処理 */
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {

		//空文字をセット
		model.addAttribute("error","");

		//メッセージをModelに登録
		model.addAttribute("message","UserRestController で例外が発生しました");

		//HTTPのエラーコード（500）をModelに登録
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR);

		return "error";
	}
}
