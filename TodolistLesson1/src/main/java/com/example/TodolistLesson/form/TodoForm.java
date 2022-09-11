package com.example.TodolistLesson.form;

import lombok.Data;

@Data
public class TodoForm {
	private Integer ItemId;	
	private Integer listId;
	private String ItemName;
	private Integer doneFlg;
	private Integer priority;
	private Integer importance;
}
