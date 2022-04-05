package com.example.TodolistLesson.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

	@Test
	void 該当ユーザが存在する場合loadUserByUsernameでユーザーが取得される() {
		String userId = "testuser1@co.jp";
		MUser user = new MUser();
		user.setUserId(userId);
		user.setAppUserName("testuser1 user1");
		user.setGender(2);
		user.setRole("ROLE_GENERAL");
		when(userMapper.findLoginUser(userId)).thenReturn(user);
		MUser actualUser = (MUser) userService.loadUserByUsername(userId);
		assertAll(
			() -> assertEquals(user.getUserId(), actualUser.getUserId()),
			() -> assertEquals(user.getAppUserName(), actualUser.getAppUserName()),
			() -> assertEquals(user.getGender(), actualUser.getGender()),
			() -> assertEquals(user.getRole(), actualUser.getRole()),
			() -> assertTrue(actualUser.isAccountNonExpired()),
			() -> assertTrue(actualUser.isAccountNonLocked()),
			() -> assertTrue(actualUser.isCredentialsNonExpired()),
			() -> assertTrue(actualUser.isEnabled())
		);
	}

	@Test
	void 該当ユーザ無しの場合loadUserByUsernameで例外が発生する() {
		String userId = "notexist@co.jp";
		when(userMapper.findLoginUser(userId)).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, () -> {
			userService.loadUserByUsername(userId);
		});
	}

	@Test
	public void getUserOneでIDヒット件数を取得できる() {
		String userId = "testuser1@co.jp";
		//setup
		when(userMapper.findOne(userId)).thenReturn(1);
		//exercise
		int count = userService.getUserOne(userId);
		//verify
		assertEquals(count, 1);
		verify(userMapper,times(1)).findOne(userId);
	}

	@Test
	public void signupで登録処理を呼び出す() {
		//setup
		MUser newUser = new MUser();
		newUser.setUserId("testuser1@co.jp");
		newUser.setAppUserName("test user1");
		newUser.setPassword("password");
		newUser.setGender(1);
		doNothing().when(userMapper).insertOne(newUser);
		//exercise
		userService.signup(newUser);
		//verify
		verify(userMapper, times(1)).insertOne(newUser);
	}

	@Test
	public void updateUserNameで更新処理を実行する() {
		String userId = "testuser1@co.jp";
		String newName = "hogehoge";
		//setup
		doNothing().when(userMapper).updateName(userId, newName);
		//exercise
		userService.updateUserName(userId, newName);
		//verify
		verify(userMapper, times(1)).updateName(userId, newName);
	}

	@Test
	public void updatePassでパスワードを暗号化して更新する() {
		String userId = "testuser1@co.jp";
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		// forClass()メソッドで、その型の容れ物（変数）をつくる
		ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
		//任意の引数でモック動作させる場合はanyが使える
		doNothing().when(userMapper).updatePass(anyString(), anyString());
		//exercise
		userService.updateUserPass(userId, "password");
		//verify
		verify(userMapper, times(1)).updatePass(anyString(), argCaptor.capture());
		assertThat(encoder.matches("password", argCaptor.getValue()));
	}

	//ユーザー削除のテスト
	@Test
	public void deleteUserで削除処理を実行する() {
		//setUp
		String userId = "testuser1@co.jp";
		doNothing().when(userMapper).deleteOne(userId);
		//exercise
		userService.deleteUser(userId);
		//verify
		verify(userMapper, times(1)).deleteOne(userId);
	}
}
