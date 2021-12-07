package com.example.TodolistLesson.rest;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestResult {
	/**リターンコード*/
	private int result;
	
	/**エラーマップ*/
	private Map<String, String>errors;

}
