package com.example.TodolistLesson.form;

import lombok.Data;

@Data
public class TodoForm {
	private int ItemId;	
	private int listId;
	private String ItemName;
	private int doneFlg;
	private int priority;
	private int importance;
}
