package com.example.TodolistLesson.domain.user.model;


import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class MUser implements UserDetails {
//==========================
// Spring で必要なフィールド
//==========================
	private String userId;
	private String password;
	private Date passUpdateDate; //不使用
	private int loginMissTimes;  //不使用
	private boolean unlok;		 //不使用
	private boolean enabled;	 //不使用
	private Date userDueDate; 	 //不使用
	//権限のCollection
	private Collection<? extends GrantedAuthority> authority;//不使用

//==========================
// Spring で必要なフィールド
//==========================
	private String appUserName;//変数名変更(useName＝idとみなされる為）
	private Integer gender;
	private String role;//これauthか？

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authority;
	}
	@Override
	public String getUsername() {
		return this.userId;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
			return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
			return true;
	}

	public boolean isEnabled() {
		return true;
	}
}
