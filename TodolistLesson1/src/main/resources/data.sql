/* ユーザーマスタ*/
/*毎回動かれてもいいようにon conflictを使う*/
--INSERT INTO m_user(
--	 user_id
--	,password
--	,user_name
--	,gender
--	,role
--)VALUES
--('user@co.jp','$2a$08$/fGFp9vDqYcbLrTxchxQluW4N43/DnDqIvAAE9GK3ZfJKFE7.OLg2','test user1', 2, 'ROLE_GENERAL')
--ON CONFLICT (user_id) DO NOTHING;
--
--/*タイトル*/
--INSERT INTO t_title(
--	 user_id
--	,list_id
--	,list_name
--	,color
--)VALUES
--('user@co.jp', 1, 'sample list', '#ffffff')
--ON CONFLICT DO NOTHING;


/*todoリスト*/
--INSERT INTO t_todo_item(
--	 list_id
--	,item_id
--	,item_name
--	,done_flg
--	,priority
--	,importance
--)VALUES
--(1, 1, 'sample todo item','0', 3, 3),
--(1, 2, 'sample todo item2','0', 3, 3)
--ON CONFLICT DO NOTHING;


/*list_id のシーケンスを更新する
SELECT SETVAL ('t_title_list_id_seq', 5);
*/

/*item_id のシーケンスを更新する
SELECT SETVAL ('t_todo_item_item_id_seq', 3);
*/
