package com.example.TodolistLesson.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.domain.user.service.UserService;
import com.example.TodolistLesson.repository.UserMapper;

@Service
@Component("UserServiceImpl")
public class UserServiceImpl implements UserService, 
	UserDetailsService  {

	@Autowired
	private UserMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;


	public UserServiceImpl() {
		
	}
	//テストで使うコンストラクタ作成する
	public UserServiceImpl(UserMapper usermapper, PasswordEncoder encoder) {
		this.mapper = usermapper;
		this.encoder = encoder;
	}
	
	/*UserDetails Service の実装*/
	/*ユーザー取得*/	
	@Override
	public UserDetails loadUserByUsername(String username) throws 
	UsernameNotFoundException {
		UserDetails user = mapper.findLoginUser(username);
		try {
			user.getUsername();//null判定用
			return user;
		}catch(NullPointerException e){
			throw new UsernameNotFoundException("user not found");				
		}
	}

	/** ユーザー取得（存在確認）*/
	public int getUserOne(String userId) {
		return mapper.findOne(userId);
	}	
	
	/*ユーザ登録*/
	@Override
	public void signup(MUser user) {
		user.setRole("ROLE_GENERAL");//ロール
		
		//パスワード暗号化
		String rawPassword = user.getPassword();
		user.setPassword(encoder.encode(rawPassword));
		
		mapper.insertOne(user);		
	}
	
	/*ユーザー名更新*/
	@Override
	public void updateUserName(String userId,
			String userName) {
		mapper.updateName(userId, userName);
	}
	
	/*ユーザーパスワード更新*/
	@Override
	public void updateUserPass(String userId, String password) {
		String encryptPassword = encoder.encode(password);
		mapper.updatePass(userId, encryptPassword);
	}
	
	/* ユーザー削除*/
	@Override
	public void deleteUser(String userId) {
		mapper.deleteOne(userId);
	}
	
}
