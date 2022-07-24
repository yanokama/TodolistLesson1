/* ユーザーマスタ*/
INSERT INTO m_user(
	 user_id
	,password
	,user_name
	,gender
	,role
)VALUES
('testuser1@co.jp','$2a$08$/fGFp9vDqYcbLrTxchxQluW4N43/DnDqIvAAE9GK3ZfJKFE7.OLg2','test user1', 2, 'ROLE_GENERAL')
ON CONFLICT (user_id) DO NOTHING;