'use strict';

/**画面ロード時の処理 */
jQuery(function($){
	/**削除ボタンを押したときの処理. */
	$('#btn-delete').click(function(event){
		//ユーザー削除
		deleteUser();
	});
});

/**ユーザー削除処理 */
function deleteUser(){
	//フォームの値を取得
	//フォーム名のIDとあわせる
	var formData = $('#user-delete-form').serializeArray();
	
	//ajax通信
	$.ajax({
		type:"DELETE",
		cache:false,
		url:'/user/delete',
		data: formData,
		dataType:'json',
	}).done(function(data){
		//ajax成功時の処理
		alert('退会しました')
		window.location.href = '/login/login'
	}).fail(function(jqXHR, textStatus, errorThrown){
		//ajax失敗時の処理
		alert('退会処理に失敗しました');
	}).always(function(){
		//常に実行する処理
	});
}