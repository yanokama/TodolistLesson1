package com.example.TodolistLesson.demo;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.UserService;
import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;
import com.example.TodolistLesson.repository.UserMapper;

public class UserServiceImplTest {
	
	UserMapper userMapper;
	UserService userService;
	
	@BeforeEach
	void setUp() {
        // UserMapperのモックを作成		
		userMapper = mock(UserMapper.class);
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		//UserMapperのモックを利用してUserServiceImplインスタンスを作成		
		userService = new UserServiceImpl(userMapper, encoder);
	}
	
	//ユーザ取得のテスト１（ログイン）
    //ユーザ取得のテスト２（存在確認）
	@Test
	public void getUserOneTest() {
		//UserMapperの戻り値を設定
		when(userMapper.findOne("testuser1@co.jp")).thenReturn(1);
		assertEquals(userService.getUserOne("testuser1@co.jp"), 1);
		verify(userMapper,times(1)).findOne("testuser1@co.jp");
	}    
    
	//ユーザ登録のテスト
	@Test
	public void signupTest() {
		MUser newUser = new MUser();
		newUser.setUserId("testuser2@co.jp");
		newUser.setAppUserName("test user2");
		newUser.setPassword("password");
		newUser.setGender(1);
		doNothing().when(userMapper).insertOne(newUser);
		userService.signup(newUser);
		verify(userMapper, times(1)).insertOne(newUser);
	}
	
	//ユーザー名更新のテスト
	@Test
	public void updateUserNameTest() {
		doNothing().when(userMapper).updateName("testuser1@co.jp", "hogehoge");
		userService.updateUserName("testuser1@co.jp", "hogehoge");
		verify(userMapper, times(1)).updateName("testuser1@co.jp", "hogehoge");
	}

	//パスワード更新のテスト
	@Test
	public void updatePassTest() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();		
		// forClass()メソッドで、その型の容れ物（変数）をつくる		
		ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
		//任意の引数でモック動作させる場合はanyが使える
		doNothing().when(userMapper).updatePass(anyString(), anyString());
		userService.updateUserPass("testuser1@co.jp", "password");
		verify(userMapper, times(1)).updatePass(anyString(), argCaptor.capture());
		assertThat(encoder.matches("password", argCaptor.getValue()));
	}	
	
	//ユーザー削除のテスト
	@Test
	public void deleteUserTest() {
		doReturn(1).when(userMapper).deleteOne("testuser1@co.jp");
		userService.deleteUser("testuser1@co.jp");
		verify(userMapper, times(1)).deleteOne("testuser1@co.jp");		
	}
}
