/* ユーザーマスタ*/
INSERT INTO t_title(
	list_id
	,user_id
	,list_name
)VALUES
(1,'testuser1@xxx.co.jp','test title');
/*ON CONFLICT (list_id) DO NOTHING;*/