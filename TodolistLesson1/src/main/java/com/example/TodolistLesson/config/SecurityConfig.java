package com.example.TodolistLesson.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.TodolistLesson.domain.user.service.impl.UserServiceImpl;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Qualifier("UserServiceImpl")
	private UserServiceImpl userServiceImpl;
	
	//パスワードエンコーダのBean定義
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**セキュリティの対象外を設定 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		//セキュリティを適用しない
		web
			.ignoring()
				.antMatchers("/webjars/**")
				.antMatchers("/css/**")
				.antMatchers("/js/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		//直リンク禁止ログイン不要のページの設定
		http
			.authorizeRequests()
				.antMatchers("/login").permitAll() //直リンクOK
				.antMatchers("/signup/signup").permitAll() //直リンクOK
				.anyRequest().authenticated(); //それ以外は直リンク禁止

		//ログイン処理の実装
		http
			.formLogin()
				.loginProcessingUrl("/login")
				.loginPage("/login")
				.failureUrl("/login?error")
				.usernameParameter("userId")
				.passwordParameter("password")
				.defaultSuccessUrl("/todo/title", true);

		//ログアウト処理の実装
		http
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login");
	}
	
	//ログイン処理時のユーザ情報をDBから取得(これいるのか？）
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	throws Exception{
		auth.userDetailsService(userServiceImpl)
		.passwordEncoder(passwordEncoder());
	}
	
}
