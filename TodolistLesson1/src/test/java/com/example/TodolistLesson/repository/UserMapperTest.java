package com.example.TodolistLesson.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import com.example.TodolistLesson.domain.user.model.MUser;
import com.example.TodolistLesson.repository.UserMapper;

import javax.sql.DataSource;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional
public class UserMapperTest {

	@Autowired
	UserMapper mapper;
    @Autowired
    private NamedParameterJdbcOperations jdbcOperations;


    @Nested //RunWith(Enclosed.class）の代わり！！
    @TestInstance(Lifecycle.PER_CLASS)
    class データ0件の場合{
		@BeforeAll
		public void setUp( @Autowired DataSource dataSource) {
			//テーブルデータ削除
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
			populator.addScript(new ClassPathResource("/muser_delete.sql"));
			populator.execute(dataSource);
		}
		@Test
		public void findOneで0が返される() {
			assertEquals(mapper.findOne("user@co.jp"), 0);
		}

		@Test
		public void  findLoginUseTestでnullが返される() {
			MUser actualUser = mapper.findLoginUser("testuser1@co.jp");
			assertThat(actualUser, nullValue());
		}

		@Test
		public void InsertOneで1件登録される() {
			//setup
			MUser newUser = new MUser();
			newUser.setUserId("testuser2@co.jp");
			newUser.setAppUserName("test user2");
			newUser.setPassword("$2a$08$/fGFp9vDqYcbLrTxchxQluW4N43/DnDqIvAAE9GK3ZfJKFE7.OLg2");
			newUser.setGender(1);
			//exercise
			mapper.insertOne(newUser);
			//test
			MUser actualUser =
				jdbcOperations.queryForObject(
					"select user_id, password, user_name as appUserName, gender "
					+ "from m_user where user_id = :id",
					new MapSqlParameterSource("id", newUser.getUserId()),
					new BeanPropertyRowMapper<>(MUser.class));
			assertThat(actualUser.getUserId(), is(newUser.getUserId()));
			assertThat(actualUser.getAppUserName(), is(newUser.getAppUserName()));
			assertThat(actualUser.getPassword(), is(newUser.getPassword()));
			assertThat(actualUser.getGender(), is(newUser.getGender()));
		}
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class データありの場合{
		@BeforeEach
		public void setUp( @Autowired DataSource dataSource) {
			//テストデータ挿入
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
			populator.addScript(new ClassPathResource("/muser_testdata.sql"));
			populator.execute(dataSource);
		}

		@Test
		public void findOneで1が返される() {
			assertEquals(mapper.findOne("testuser1@co.jp"), 1);
		}

		@Test
		public void  findLoginUserでユーザーを取得できる() {
			MUser expected = new MUser();
			expected.setUserId("testuser1@co.jp");
			expected.setAppUserName("test user1");
			expected.setPassword("$2a$08$/fGFp9vDqYcbLrTxchxQluW4N43/DnDqIvAAE9GK3ZfJKFE7.OLg2");
			expected.setGender(2);

			MUser actualUser = mapper.findLoginUser("testuser1@co.jp");
			assertThat(actualUser.getUserId(), is(expected.getUserId()));
			assertThat(actualUser.getAppUserName(), is(expected.getAppUserName()));
			assertThat(actualUser.getPassword(),is(expected.getPassword()));
			assertThat(actualUser.getGender(),is(expected.getGender()));
		}

		@Test
		public void updateNameで名前を変更できる() {
			MUser expected = new MUser();
			expected.setUserId("testuser1@co.jp");
			expected.setAppUserName("test user1 update");
			expected.setPassword("$2a$08$/fGFp9vDqYcbLrTxchxQluW4N43/DnDqIvAAE9GK3ZfJKFE7.OLg2");
			expected.setGender(2);
			mapper.updateName(expected.getUserId(), expected.getAppUserName());

			MUser actualUser =
				jdbcOperations.queryForObject(
					"select user_id, password, user_name as appUserName, gender "
					+ "from m_user where user_id = :id",
					new MapSqlParameterSource("id", expected.getUserId()),
					new BeanPropertyRowMapper<>(MUser.class));
			assertThat(actualUser.getUserId(), is(expected.getUserId()));
			assertThat(actualUser.getAppUserName(), is(expected.getAppUserName()));
			assertThat(actualUser.getPassword(), is(expected.getPassword()));
			assertThat(actualUser.getGender(), is(expected.getGender()));
		}

		@Test
		public void deleteOneでデータが削除される() {
	        int count;
	        String targetId = "testuser1@co.jp";
			//exercise
	        mapper.deleteOne(targetId);
	        //test
	        count = jdbcOperations.queryForObject (
	        		"select count(user_id) from m_user where user_id = :id ",
	        		new MapSqlParameterSource("id", targetId),
	        		Integer.class);
			assertThat(count, is(0));
		}
    }
}
