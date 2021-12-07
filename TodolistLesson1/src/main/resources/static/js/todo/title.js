'use strict';

/**画面ロード時の処理 */
jQuery(function($){
	/**削除ボタンを押したときの処理 */
	$('.title-delete-btn').on('click',function(){
		if (confirm('タイトルを削除します。よろしいですか？')) {
			$(this).parent('form').submit();
		} else {
		  return false
		}
	});	
});
