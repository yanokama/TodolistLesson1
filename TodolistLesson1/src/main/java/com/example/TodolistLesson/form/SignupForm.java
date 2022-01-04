package com.example.TodolistLesson.form;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class SignupForm {
	
	@NotBlank(groups = ValidGroup1.class)
	@Email(groups = ValidGroup2.class)
	private String userId;
	
	@NotBlank(groups = ValidGroup1.class)
	@Length(min = 8, max = 100, groups = ValidGroup2.class)
	@Pattern(regexp = "^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$", groups = ValidGroup2.class)
	private String password;
	
	@NotBlank(groups = ValidGroup1.class)
	private String appUserName;
	
	@NotNull(groups = ValidGroup1.class)
	private Integer gender;
	
}
