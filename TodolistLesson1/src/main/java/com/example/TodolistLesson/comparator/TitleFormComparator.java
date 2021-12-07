package com.example.TodolistLesson.comparator;

import java.util.Comparator;

import com.example.TodolistLesson.form.TitleForm;

public class TitleFormComparator implements Comparator<TitleForm>{
	@Override
	public int compare(TitleForm title1, TitleForm title2) {
		return title1.getListId() < title2.getListId() ? -1 : 1;
	}

}
