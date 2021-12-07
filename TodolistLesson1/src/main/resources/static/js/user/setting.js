'use strict';

/**画面ロード時の処理 */
jQuery(function($){
	/**パスワード変更ボタンを押したときの処理 */
	$('#btn-update-pass').click(function(event){
		if (confirm('パスワードを更新します。よろしいですか？')) {
			//パスワード変更
			updatePass();
		} else {
		  return false
		}
	});
	/**ユーザー名変更ボタンを押したときの処理 */
	$('#btn-update-name').click(function(event){
		if (confirm('ユーザー名を更新します。よろしいですか？')) {
			//ユーザー名変更
			updateName();
		} else {
		  return false
		}			
	});
	
	
});

/**パスワード変更処理 */
function updatePass(){
	//バリデーション結果をクリア
	removeValidResult();
	//フォームの値を取得
	var formData = $('#user-setting-form').serializeArray();
	
	//ajax通信
	$.ajax({
		type: "PUT",
		chache:false,
		url:'/user/updatePass',
		data: formData,
		dataType: 'json',
	}).done(function(data){
		//ajax成功時の処理
		console.log(data);
		if(data.result===90){
			//validationエラー時の処理
			$.each(data.errors, function(key, value){
				reflectValidResult(key, value)
			});	
		}else if(data.result===0){
			alert('パスワードを変更しました');
			$("#password").val("");
		}
	}).fail(function(jqXHR, textStatus, errorThrown){
		//ajax失敗時の処理
		alert('パスワード変更に失敗しました');
	}).always(function(){
		//常に実行する処理
	});
}

/**ユーザー名変更処理 */
function updateName(){
	//バリデーション結果をクリア
	removeValidResult();
	//フォームの値を取得
	var formData = $('#user-setting-form').serializeArray();
	
	//ajax通信
	$.ajax({
		type: "PUT",
		chache:false,
		url:'/user/updateName',
		data: formData,
		dataType: 'json',
	}).done(function(data){
		//ajax成功時の処理
		console.log(data);
		if(data.result===90){
			//validationエラー時の処理
			$.each(data.errors, function(key, value){
				reflectValidResult(key, value)
			});	
		}else if(data.result===0){
			alert('ユーザー名を変更しました');
		}
	}).fail(function(jqXHR, textStatus, errorThrown){
		//ajax失敗時の処理
		alert('ユーザー名変更に失敗しました');
	}).always(function(){
		//常に実行する処理
	});
}

/**バリデーション結果をクリア*/
function removeValidResult(){
	$('.is-invalid').removeClass('is-invalid');
	$('.invalid-feedback').remove();
	$('.text-danger').remove();
}

/**バリデーション結果の反映 */
function reflectValidResult(key, value){
	//エラーメッセージ追加
	if(key==='gender'){	//性別の場合
		//CSS適用
		$('input[name=' + key + ']').addClass('is-invalid');
		//エラーメッセージ追加
		$('input[name=' + key + ']')
			.parent().parent()
			.append('<div class="text-danger>"' + value + '</div>');
	}else{	//性別以外の場合
		//CSS適用
		$('input[id=' + key + ']').addClass('is-invalid');
		//エラーメッセージ追加
		$('input[id=' + key + ']')
			.after('<div class="invalid-feedback">' + value + '</div>');
	}
}