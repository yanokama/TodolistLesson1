package com.example.TodolistLesson.domain.user.model;

import lombok.Data;

@Data
public class Todo {
	private int itemId;	
	private int listId;
	private String ItemName;
	private int doneFlg;
	private int priority;
	private int importance;

}
