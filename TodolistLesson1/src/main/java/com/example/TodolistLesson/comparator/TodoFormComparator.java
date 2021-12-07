package com.example.TodolistLesson.comparator;

import java.util.Comparator;

import com.example.TodolistLesson.form.TodoForm;

public class TodoFormComparator implements Comparator<TodoForm>{
	@Override
	public int compare(TodoForm todo1, TodoForm todo2) {
		return todo1.getItemId() < todo2.getItemId() ? -1 : 1;
	}
}
