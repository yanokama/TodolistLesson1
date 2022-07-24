package com.example.TodolistLesson.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

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

import com.example.TodolistLesson.domain.user.model.Title;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoMapperTest {

	@Autowired
	TodoMapper mapper;
    @Autowired
    private NamedParameterJdbcOperations jdbcOperations;
    /**
     * 0件の場合
		findtitle = 0件のリスト
	findtitleOne = nullのTitle
	insertTitle →　SQLを使って追加されたことを検証
	updateTitle →　やらない
	deleteTitle →　やらなくていいのでは？
	deleteUsersTitle →　やらなくてよい
	*/
    @Nested 
    @TestInstance(Lifecycle.PER_CLASS)
    class データ0件の場合{
		@BeforeAll
		public void setUp( @Autowired DataSource dataSource) {
			//テーブルデータ削除
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
			populator.addScript(new ClassPathResource("/todo_delete.sql"));
			populator.execute(dataSource);
			
		}
		@Test
    	public void findTitleは空のリストを返す() {
    		ArrayList<Title> expected = new ArrayList<>();
    		assertEquals(expected, mapper.findTitle("testuser1@xxx.co.jp"));
    	}
		@Test
		public void findTitleOneはnullのオブジェクトを返す() {
			Title expected = null;
			assertEquals(expected, mapper.findTitleOne(1));
		}
		@Test
    	public void insertTitleで1件追加できる() {
    		Title newTitle = new Title();
    		newTitle.setUserId("testuser1@xxx.co.jp");
    		newTitle.setListName("test title");
    		newTitle.setColor("RED");
    		//exercise
    		mapper.insertTitle(newTitle);
    		//verify
    		Title actualTitle =
					jdbcOperations.queryForObject(
					"select * from t_title where user_id = :id",
					new MapSqlParameterSource("id", newTitle.getUserId()),
					new BeanPropertyRowMapper<>(Title.class));
    		assertEquals(newTitle.getListId(), actualTitle.getListId());
    		assertEquals(newTitle.getUserId(), actualTitle.getUserId());
    		assertEquals(newTitle.getListName(), actualTitle.getListName());
    		assertEquals(newTitle.getColor(), actualTitle.getColor().strip());	
  
    	}
		
		public void updateTitleでタイトルを更新できる() {
    		Title newTitle = new Title();
    		newTitle.setUserId("testuser1@xxx.co.jp");
    		newTitle.setListName("updated title");
    		newTitle.setColor("RED");			
		}		
    }
    
    /**

	1件ある場合
	findtitle =　1件のリスト
	findtitleOne = Titleオブジェクト
	insertTitle →　SQLを使って追加されたことを検証、件数が2件になることを確認
	updateTitle →　SQLを使って追加されたことを検証、件数が1件のことを確認
	deleteTitle →　件数が0件になることを確認
	deleteUsersTitle →　件数が0件になることを確認

	2件ある場合（別のユーザ）
	deleteUsersTitle →　件数が１件になることを確認
     * */
    @Nested
    class データが1件ある場合 {
    	Title title = new Title();;
		@BeforeEach
		public void setUp( @Autowired DataSource dataSource) {
			//テーブルデータ削除
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
			populator.addScript(new ClassPathResource("/todo_delete.sql"));
			populator.execute(dataSource);
			//1件追加
			populator.addScript(new ClassPathResource("/title_testdata.sql"));
			populator.execute(dataSource);
			
			title.setListId(1);
    		title.setUserId("testuser1@xxx.co.jp");
    		title.setListName("test title");
    		//カラーはチェック対象外（ちなみに７桁でないと空白が入る）
    		//title.setColor("");
			
		}
		@Test
    	public void findTitleで１件のリストが取得できる() {
    		ArrayList<Title> expectedList = new ArrayList<>();
    		expectedList.add(title);
    		assertIterableEquals(expectedList, mapper.findTitle("testuser1@xxx.co.jp"));
    	}
    	public void findTitleOneでタイトルオブジェクトが取得できる() {
    		
    	}
    	public void insertTitleで１件追加できる() {
    		
    	}
    	public void updateTitleで更新できる() {
    		
    	}
    	public void deleteTitleで１件削除できる() {
    		
    	}
    	public void deleteUsersTitleで削除できる() {
    		
    	}
    }
    
    class 複数ユーザのデータがある場合 {
    	public void findTitleで指定したユーザのタイトルが取得できる() {
    		
    	}
    	public void deleteUsersTitleで指定したユーザのタイトルが削除できる() {
    		
    	}
    }
}
