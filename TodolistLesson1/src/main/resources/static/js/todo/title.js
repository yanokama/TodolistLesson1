'use strict';

/**画面ロード時の処理 */
jQuery(function($){
    var token = $('meta[name="_csrf"]').attr("content");
    var header = $('meta[name="_csrf_header"]').attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });	
    
	/**追加ボタンを押したときの処理 */
	$('#btn-create').on('click',function(){
        ///btnのname属性値とJava側のメソッドが対応する
	    const param = $(this).attr('name')		
		$.ajax({
		      type: 'PUT',
		      dataType: 'json',
		      url: '/todo/title',
		      data: param
		    }).done(function(response, textStatus, jqXHR ) {
				///引数の意味確認と必要に応じて直す（少なくともtitleJsonに置き換えは必要）
				alert("OK.");///テスト用
				let titleJson = response.titles[0]	//必ず1番目の要素に格納される。他は空き。
				addLine(titleJson);					
		    }).fail(function(jqXHR, textStatus, errorThrown) {
				alert("Failed.");
		});
	});
	
	/**新しい行の追加 */
	function addLine(titleJson){
        let clone = $('#titles tr:first').clone(true);
        clone.find('.list-name').val(titleJson.listName);        
        const url = '/todo/todo/' + titleJson.listId;
        clone.find('.list-id').attr('href', url);
        ///TODO：この際delete側のlistIdも更新する  
        $('#titles').append(clone[0]);
	}		

	/**削除ボタンを押したときの処理 */
	$('.title-delete-btn').on('click',function(){
		if (confirm('タイトルを削除します。よろしいですか？')) {
			$(this).parent('form').submit();
		} else {
		  return false
		}
	});	
});
