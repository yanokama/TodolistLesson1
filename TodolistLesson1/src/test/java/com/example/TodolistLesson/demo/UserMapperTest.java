package com.example.TodolistLesson.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.repository.UserMapper;


@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserMapperTest {
	@Autowired
	UserMapper mapper;
    @Autowired
    private NamedParameterJdbcOperations jdbcOperations; 
	
	//Selectのテスト１（カウント）
	@Test
	public void findOneTest1() {
		assertEquals(mapper.findOne("user@co.jp"), 1);
	}
	
	//Selectのテスト２（ログインユーザー）
	@Test
	@Sql("/testdata.sql")
	public void  findLoginUseTest1() {
		MUser newUser = new MUser();
		newUser.setUserId("testuser1@co.jp");
		newUser.setAppUserName("test user1");
		newUser.setPassword("$2a$08$/fGFp9vDqYcbLrTxchxQluW4N43/DnDqIvAAE9GK3ZfJKFE7.OLg2");
		newUser.setGender(2);
		
		MUser actualUser = mapper.findLoginUser("testuser1@co.jp");
		assertEquals(actualUser.getUserId(),newUser.getUserId());
		assertEquals(actualUser.getAppUserName(),newUser.getAppUserName());
		assertEquals(actualUser.getPassword(),newUser.getPassword());
		assertEquals(actualUser.getGender(),newUser.getGender());
				
	}
	
	//Insertのテスト
	@Test
	public void InsertOneTest() {
		MUser newUser = new MUser();
		newUser.setUserId("testuser2@co.jp");
		newUser.setAppUserName("test user2");
		newUser.setPassword("$2a$08$/fGFp9vDqYcbLrTxchxQluW4N43/DnDqIvAAE9GK3ZfJKFE7.OLg2");
		newUser.setGender(1);
		mapper.insertOne(newUser);
		
		MUser actualUser =
			jdbcOperations.queryForObject(
				"select user_id, password, user_name as appUserName, gender "
				+ "from m_user where user_id = :id",
				new MapSqlParameterSource("id", newUser.getUserId()),
				new BeanPropertyRowMapper<>(MUser.class));
		assertEquals(actualUser.getUserId(), newUser.getUserId());
		assertEquals(actualUser.getAppUserName(), newUser.getAppUserName());
		assertEquals(actualUser.getPassword(), newUser.getPassword());		
		assertEquals(actualUser.getGender(), newUser.getGender());	
	}
	
	//updateのテスト
	@Test
	@Sql("/testdata.sql")	
	public void updateOneTest() {
		MUser newUser = new MUser();
		newUser.setUserId("testuser1@co.jp");
		newUser.setAppUserName("test user1 update");
		newUser.setPassword("$2a$08$/fGFp9vDqYcbLrTxchxQluW4N43/DnDqIvAAE9GK3ZfJKFE7.OLg2");
		newUser.setGender(2);
		mapper.updateOne(newUser.getUserId(), newUser.getAppUserName());
		
		MUser actualUser =
			jdbcOperations.queryForObject(
				"select user_id, password, user_name as appUserName, gender "
				+ "from m_user where user_id = :id",
				new MapSqlParameterSource("id", newUser.getUserId()),
				new BeanPropertyRowMapper<>(MUser.class));
		assertEquals(actualUser.getUserId(), newUser.getUserId());
		assertEquals(actualUser.getAppUserName(), newUser.getAppUserName());
		assertEquals(actualUser.getPassword(), newUser.getPassword());		
		assertEquals(actualUser.getGender(), newUser.getGender());			
	}
	
	//deleteのテスト
	@Test
	@Sql("/testdata.sql")	
	public void deleteOneTest() {
        int count = mapper.deleteOne("testuser1@co.jp");
		assertEquals(count, 1);
	}
}
