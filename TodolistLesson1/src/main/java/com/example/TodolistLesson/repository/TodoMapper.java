package com.example.TodolistLesson.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.TodolistLesson.domain.user.model.Title;
import com.example.TodolistLesson.domain.user.model.Todo;


@Mapper
public interface TodoMapper {
	/**title取得*/
	public List<Title> findTitle(String userId);

	/**title取得*/
	public Title findTitleOne(Integer listId);

	/**Title登録*/
	public int insertTitle(Title title);

	/**Title更新*/
	public int updateTitle(Title title);	
	
	/**Title削除 */
	public int deleteTitle(	Integer listId );

	/**ユーザーに紐つくTitle削除 */
	public int deleteUsersTitle( String IuserId );
	
	/**todo 取得*/
	public List<Todo> findTodo(int listId);
	
	/**todo 取得*/
	public List<Todo> findCompletedTodo(int listId);
	
	/**todo登録*/
	public int insertTodo(Todo todo);
	
	/**todo 更新*/
	public void updateTodo(	Todo todo);
	
	/**todo 削除*/
	public void deleteTodo( Integer itemId);

	/**Title単位でのtodo 削除*/
	public void deleteTodoByTitle( Integer listId);

	/**ユーザーに紐つくtodo全削除*/
	public void deleteUsersTodo( @Param("listIds") List<Integer> listIds);	

}
