'use strict';

jQuery(function($){
    var token = $('meta[name="_csrf"]').attr("content");
    var header = $('meta[name="_csrf_header"]').attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });
	
	/**タイトルを編集したときの処理 */
	$('#todo-title input').change(function(){	
		const title = $(this).parents('#todo-title');
		const listId = title.find('input[name="listId"]').val();
	    const userId = title.find('input[name="userId"]').val();
	    const listName = title.find('input[name="listName"]').val();
	    const color = title.find('input[name="color"]').val();
		///test用 alert(listId + userId + listName + color);
		$.ajax({
		      type: 'PUT',
		      dataType: 'json',
		      url: '/todo/title',
		      data: {
				update : '',
				listId   : listId,
				userId   : userId,
				listName : listName,
				color    : color			
		      }
		    }).done(function(response, textStatus, jqXHR ) {
				///test用 alert("ok")
				///TODO:リターンコードに応じてエラーがあ0る場合はエラーメッセージを表示
			}).fail(function(jqXHR, textStatus, errorThrown) {
				alert("Failed.");
		});			
	});	
	
	/**todoを更新した時の処理 */	
	$('tbody input, tbody select').change(function(){		
	    const todo = $(this).parents('.todo');
	    const itemId = todo.find('input[name="itemId"]').val();
	    const listId = todo.find('input[name="listId"]').val();
	    const itemName = todo.find('input[name="itemName"]').val();
	    const priority = todo.find('select[name="priority"]').val();
	    const importance = todo.find('select[name="importance"]').val();
	    const is_done = todo.find('input[name="doneFlg"]').prop("checked");
	    let doneFlg;
	    if(is_done == true) {
	      doneFlg = 1;
	    }else{
	      doneFlg = 0;
	    }
	    const params = {
	        itemId : itemId,
	        listId : listId,
	        itemName : itemName,
	        doneFlg : doneFlg,
			priority : priority,
			importance : importance
	    }
		$.ajax({
		      type: 'POST',
		      dataType: 'json',
		      url: '/todo/todo/update',
		      data: params,
		    }).done(function(response, textStatus, jqXHR ) {
				window.location.href='/todo/todo/' + listId ;
		    }).fail(function(jqXHR, textStatus, errorThrown) {
				alert("Failed.");
				window.location.href='/todo/todo/' + listId ;
		});	
	});
});