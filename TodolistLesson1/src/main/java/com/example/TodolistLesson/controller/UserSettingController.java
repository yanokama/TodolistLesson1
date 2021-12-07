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
	@Autowired
	private UserService userService;

	
	@GetMapping("/setting")
	public String getUser(Model model,
		@AuthenticationPrincipal MUser user,
		@ModelAttribute("userForm") UserSettingForm form) {
		
		form = modelMapper.map(user, UserSettingForm.class);
		model.addAttribute("userForm", form);
		return "user/setting";
	}
	
	/**ユーザー名更新*/
	/*		@PostMapping(value = "/setting" , params = "updateName")	
			public String updateUsername(
					@ModelAttribute("userForm") @Validated(ValidGroup1.class) UserSettingForm form, 
					BindingResult bindingResult,
					Model model) {
	
				if(bindingResult.hasErrors()) {			
					model.addAttribute("userForm", form);
					return "user/setting";
				}
				
				userService.updateUserName(form.getUserId(),
						form.getAppUserName());
		
				//更新成功したら、認証情報も変更
				UserDetails user = userService.loadUserByUsername(form.getUserId());
				SecurityContext context = SecurityContextHolder.getContext();
				context.setAuthentication(new UsernamePasswordAuthenticationToken(
						user, user.getPassword(), user.getAuthorities()));
		
				return "redirect:/user/setting";
			}*/

	/**パスワード更新*/
	/*		@PostMapping(value = "/setting" , params = "updatePass")	
			public String updatePass(
					@ModelAttribute("userForm") @Validated(ValidGroup2.class) UserSettingForm form, 
					BindingResult bindingResult,
					Model model) {
	
				if(bindingResult.hasErrors()) {			
					model.addAttribute("userForm", form);
					return "user/setting";
				}
				
				userService.updateUserPass(form.getUserId(),
						form.getPassword());
		
				//更新成功したら、認証情報も変更
				UserDetails user = userService.loadUserByUsername(form.getUserId());
				SecurityContext context = SecurityContextHolder.getContext();
				context.setAuthentication(new UsernamePasswordAuthenticationToken(
						user, user.getPassword(), user.getAuthorities()));
		
				return "redirect:/user/setting";
			}*/
}
