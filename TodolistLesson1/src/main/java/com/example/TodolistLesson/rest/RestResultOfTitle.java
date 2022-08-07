package com.example.TodolistLesson.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.TodolistLesson.domain.user.model.Title;

import lombok.Data;

@Data
public class RestResultOfTitle extends CommonRestResult {
	private List<Title> titles;
	/*titleなし*/
	public RestResultOfTitle(
			int result, 
			Map<String, String> errors
			) {
		super(result, errors);
	}
	
	/*title1件*/
	public RestResultOfTitle(
			int result, 
			Map<String, String> errors,
			Title title
			) {
		super(result, errors);
		titles = new ArrayList<>(){{add(title);}};	
	}

}
