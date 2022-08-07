package com.example.TodolistLesson.rest;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonRestResult {
	/**リターンコード*/
	private int result;
	
	/**エラーマップ*/
	private Map<String, String> errors;

}
