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
				///TODO:リターンコードに応じてエラーがある場合はエラーメッセージを表示
			}).fail(function(jqXHR, textStatus, errorThrown) {
				alert("Failed.");
		});			
	});	
	
	/**追加ボタンを押したときの処理 */
	$('#btn-create').on('click',function(){
	    const listId = $(this).prev('.listid').val();	
		$.ajax({
		      type: 'PUT',
		      dataType: 'json',
		      url: '/todo/todo',
		      data: {
			   insert : '',
			   listId : listId
			  }
		 
		    }).done(function(response, textStatus, jqXHR ) {
				//テーブルが0行の時はコピーできないので画面更新する必要がある
				if(tableIsEmpty('#todos')){
					window.location.href = '/todo/todo/' + listId;					
				}else{
					//必ず1番目の要素に格納される。他は空き。
					let todoJson = response.todos[0];					
					addLine('#todos', todoJson);										
				}
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
	    let doneFlg = (is_done)? 1 : 0;
	    
		const rowBeforeUpdate = $(this).closest('tr');
		const isDoneFlg = $(this).prop('name') == 'doneFlg'  			    
		$.ajax({
		      type: 'PUT',
		      dataType: 'json',
		      url: '/todo/todo',
		      data: {
				update : '',
		        ItemId : itemId,
		        listId : listId,
		        ItemName : itemName,
		        doneFlg : doneFlg,
				priority : priority,
				importance : importance
			  }
		    }).done(function(response, textStatus, jqXHR ) {
				if(response.result > 0){
					alert("更新に失敗しました");
					window.location.href='/todo/todo/' + listId ;									
				}
		
				let updatedTodo = response.todos[0];
				if(isDoneFlg){
					$(rowBeforeUpdate).remove();	
					addLineToAnotherTable(updatedTodo);
				}				
		    }).fail(function(jqXHR, textStatus, errorThrown) {
				alert("更新に失敗しました");
				window.location.href='/todo/todo/' + listId ;
		});	
	});
	
	/**削除ボタンを押したときの処理 */
	$('.todo-delete-btn').on('click',function(){
	    const todo = $(this).parents('.todo');
	    const itemId = todo.find('input[name="itemId"]').val();
	    const listId = todo.find('input[name="listId"]').val();
	    const itemName = todo.find('input[name="itemName"]').val();
	    const priority = todo.find('select[name="priority"]').val();
	    const importance = todo.find('select[name="importance"]').val();
	    const is_done = todo.find('input[name="doneFlg"]').prop("checked"); 
	    let doneFlg = (is_done)? 1 : 0;		
		const rowBeforeUpdate = $(this).closest('tr');		
		$.ajax({
		      type: 'PUT',
		      dataType: 'json',
		      url: '/todo/todo',
		      data: {
				delete : '',
		        ItemId : itemId,
		        listId : listId,
		        ItemName : itemName,
		        doneFlg : doneFlg,
				priority : priority,
				importance : importance		      	
		      }
		    }).done(function(response, textStatus, jqXHR ) {
				$(rowBeforeUpdate).remove();
			}).fail(function(jqXHR, textStatus, errorThrown) {
				alert("Failed.");
		});	
	});
		
	
	/**ユーティリティ */
	/**テーブルが空か否かの確認 */
	function tableIsEmpty(targetTbody){
		let len = $(targetTbody + '>tr' ).length;
		return len === 0;
	}
	
	/**新しい行の追加 */
	function addLine(targetTbody, todoJson){
        const clone = $( targetTbody + ' tr:first').clone(true);
        ///チェックボックスはテーブルによって決まるので更新してない
        clone.find('input[name="itemId"]').val(todoJson.itemId);    
        clone.find('input[name="itemName"]').val(todoJson.itemName);        
        clone.find('select[name="priority"]').val(todoJson.priority);
        clone.find('select[name="importance"]').val(todoJson.importance);        
        $(targetTbody).append(clone[0]);
	}
	
	function addLineToAnotherTable(updatedTodo){
		let targetTbody = (updatedTodo.doneFlg === 0)? '#todos' : '#done-todos';
		
		if(tableIsEmpty(targetTbody)){
			window.location.href = '/todo/todo/' + updatedTodo.listId;
		}else{
			addLine(targetTbody, updatedTodo)
		}
	}	
});