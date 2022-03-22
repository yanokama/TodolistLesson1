package com.example.TodolistLesson.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.TodolistLesson.domain.user.model.MUser;



@Mapper
public interface UserMapper {

	/**ユーザー登録*/
	public void insertOne(MUser user);

	/**ユーザー更新 */
	public void updateName(@Param("userId") String userId,
			@Param("userName") String userName);

	/**ユーザーパスワード更新 */
	public void updatePass(@Param("userId") String userId,
			@Param("password") String password);

	/**ユーザー削除（1件）*/
	public void deleteOne(String userId);

	/**ログインユーザー情報*/
	public MUser findLoginUser(String userId);

	/**ユーザー取得(存在確認）*/
	public int findOne(String userId);
}
