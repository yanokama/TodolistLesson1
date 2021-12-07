/*ユーザーマスタ*/
CREATE TABLE IF NOT EXISTS m_user(
	 user_id VARCHAR(50) PRIMARY KEY
	,password VARCHAR(100)
	,user_name VARCHAR(50)
	,gender INT
	,role VARCHAR(50)
);

/*リストタイトル*/
CREATE TABLE IF NOT EXISTS t_title(
	list_id SERIAL PRIMARY KEY
	,user_id VARCHAR(50) 
	,list_name VARCHAR(50)
	,color CHAR(7)
);

/*todoリスト*/
CREATE TABLE IF NOT EXISTS t_todo_item(
	item_id SERIAL PRIMARY KEY
	,list_id SERIAL 
	,item_name VARCHAR(40)
	,done_flg NUMERIC(1) DEFAULT 0
	,priority INT
	,importance INT
);

/*list_idのシーケンス*/
CREATE SEQUENCE if not exists list_id_seq
 START WITH 1
 INCREMENT BY 1
 MAXVALUE 1000000
 NO CYCLE;
 