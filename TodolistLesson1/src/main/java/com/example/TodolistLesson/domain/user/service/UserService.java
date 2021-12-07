package com.example.TodolistLesson.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.example.TodolistLesson.domain.user.model.MUser;



public interface UserService {

	/**ユーザー登録*/
	public void signup(MUser user);
	
	/** ユーザー更新（１件）*/
	public void updateUserName(String userId,
			String userName);

	/** ユーザーパスワード更新*/
	public void updateUserPass(String userId,
			String password);
	
	/** ユーザー削除（１件）*/
	public void deleteUser(String userId);

	/** ログインユーザー情報取得*/
	public UserDetails loadUserByUsername(String username);	
	
	/** ユーザー取得（存在確認）*/
	public int getUserOne(String userId);	
}
