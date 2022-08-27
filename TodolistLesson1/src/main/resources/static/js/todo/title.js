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
				let titleJson = response.titles[0]	//必ず1番目の要素に格納される。他は空き。
				addLine(titleJson);					
		    }).fail(function(jqXHR, textStatus, errorThrown) {
				alert("Failed.");
		});
	});
	
	/**削除ボタンを押したときの処理 */
	$('.title-delete-btn').on('click',function(){
		if (confirm('タイトルを削除します。よろしいですか？')) {	
			const listId = $(this).prev('.delete-list-id');
			///ｄoneの下で宣言すると意図した動きをしない。理由をよくわかっていない。
			let row = $(this).closest('tr');
			alert(listId.val());
			$.ajax({
			      type: 'PUT',
			      dataType: 'json',
			      url: '/todo/title',
			      data: {
					'delete' : '',
			      	'listId' : listId.val()
			      }
			    }).done(function(response, textStatus, jqXHR ) {
 					$(row).remove();
  				}).fail(function(jqXHR, textStatus, errorThrown) {
					alert("Failed.");
			});	
		} else {
		  return false
		}
	});
	
	/**ユーティリティ */
	/**新しい行の追加 */
	function addLine(titleJson){
        let clone = $('#titles tr:first').clone(true);
        clone.find('.list-name').val(titleJson.listName);        
        const url = '/todo/todo/' + titleJson.listId;
        clone.find('.list-id').attr('href', url);
        clone.find('.delete-list-id').attr('value', titleJson.listId);
        $('#titles').append(clone[0]);
	}
			
});
