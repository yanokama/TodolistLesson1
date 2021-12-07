package com.example.TodolistLesson.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserSettingForm {
	private String userId;
	@NotBlank(groups = ValidGroup2.class)
	@Length(min = 8, max = 100, groups = ValidGroup2.class )
	@Pattern(regexp ="^[a-zA-Z0-9 -/:-@\\[-\\`\\{-\\~]+$", groups = ValidGroup2.class)
	private String password;
	@NotBlank(groups = ValidGroup1.class)
	private String appUserName;
	private Integer gender;
	//private String role;
}



