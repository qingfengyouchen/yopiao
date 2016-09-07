/**
 * 扩展插件
 * @param {Object}
 * @memberOf {TypeName} 
 */
(function($){
	$.extend($.fn, {
		initTable: function(opts, $father){//初始化表，添加奇偶行背景色及鼠标悬停行背景色，全选、反选
			opts = $.extend( {
				namespace: '', 
				listMethod:'/list-content',
				listPageMethod: '/list',
				addMethod:'/create',
				editMethod:'/edit/{id}',
				/*addSaveMethod:'/create',
				editSaveMethod:'/{id}/edit',*/
				viewMethod: '/view/{id}',
				view1Method: '/view1/{id}',
				deleteMethod:'/delete',
				btnAddId: 'btnAdd',
				btnDeleteId: 'btnDelete',
				btnSearchId: 'btnSearch',
				formId:'frmSearch',
				isBindDblClick: true,//是否绑定行单击事件
				isDblClickToEdit: true,//单击是否修改，否则是查看
				isCheckAll: true,
				isBindEnter: false,
				checkboxId: 'ids',
				selectedBgColorClass: "info",
				beforeAddCallback: undefined
			}, opts); 
			
			var $form = $('#'+opts.formId);
			if($form.length < 1){
				alert("请设置表单id");
				return;
			}else{
				if(Public.isNull($form.attr('action'))){
					alert("请设置表单的action属性");
					return;
				}
			}
			var $this = $(this);
			if (Public.isIE6()){//IE6防止被 返回顶部层 遮住
				$this.css({
					position: 'relative',
					'z-index': 10
				});
			}

			if($father != undefined && $father.length > 0)
				$this = $father;

			/*var rowLen = $this.find('tr').length;
			if( $this.find('tr:last .page').length > 0 )
				rowLen--;

			var $content = $this.find('tr').slice(1, rowLen);
			$content.filter(':odd').addClass(opts.oddBgColorClass);
			$content.filter(':even').addClass(opts.evenBgColorClass);*/
			/*var className;
			$content.unbind().bind({
				mouseover: function(){
					className = $(this).attr('class');
					$(this).removeClass().addClass(opts.overBgColorClass);
				},
				mouseout: function(){
					$(this).removeClass().addClass(className);
				}
			});*/

			$this.find('tr:gt(0) input:checkbox').unbind('click').click(function(e){
				/*var _this = $(this);
				if(_this.attr('checked')){
					_this.attr('checked', false);
					_this.parent().parent().removeClass(opts.selectedBgColorClass);
				}else{
					_this.attr('checked', true);
					_this.parent().parent().addClass(opts.selectedBgColorClass);
				}*/
				Public.stopBubble(e);
			});

			$this.find('tr:gt(0)').unbind('click').click(function(e){
				var $checkbox = $(this).find('td:first input:checkbox');
				if($checkbox.length < 1)
					return;
				if($checkbox.attr('checked') == 'checked'){
					$checkbox.attr('checked', false);
					$(this).removeClass("info");
				}else{
					$checkbox.attr('checked', true);
					$(this).addClass("info");
				}
			});

			if(opts.isBindDblClick){
				$this.find('tr:gt(0)').unbind('dblclick').dblclick(function(event){
					if(opts.isClickToEdit){
						$(this).find('.aedit').click();
					}else{
						$(this).find('.aview').click();
					}
				});
			}

			//全选
			if(opts.isCheckAll){
				$this.find(':checkbox:first').unbind('click').click(function(){
					if($(this).attr('checked')){
						selectAll($this, opts.selectedBgColorClass);
					}else{
						unselectAll($this, opts.selectedBgColorClass);
					}
				});
			}
			//新增
			$form.find('#'+opts.btnAddId).unbind('click').click(function(){
				if(opts.beforeAddCallback){
					if(opts.beforeAddCallback()){
						$this.addEntity(opts);
					}
				}else{
					$this.addEntity(opts);
				}
			});
			//查询
			$form.find('#'+opts.btnSearchId).unbind('click').click(function(){
				$form.find("#pageNo").val(1);
				$this.searchList(opts);
			});
			//回车键查询
			/*if(opts.isBindEnter){
				$(document).unbind('keydown').keydown(function(e){
					if(e.keyCode == 13) {
						$this.searchList(opts);
						return false;
					}
				});
			}*/
			//删除按钮
			$form.find('#'+opts.btnDeleteId).unbind('click').click(function(){
				$this.removeSeletedEntity(opts);
				return false;
			});
			//删除链接
			$this.find('.adelete').unbind('click').click(function(){
				var id = $(this).attr('id');
				var cls = $(this).attr('class');
				if (cls.indexOf("read") > 0) {
					$this.statusEntity(id, opts);
				} else if (cls.indexOf("isdone") > 0) {
					$this.tixianEntity(id, opts);
				} else {
					$this.removeEntity(id, opts);
				}
				
				return false;
			});
			//修改链接
			$this.find('.aedit').unbind('click').click(function(){
				var id = $(this).attr('id');
				viewEdit(id, opts);
				return false;
			});
			//查看链接
			$this.find('.aview').unbind('click').click(function(){
				var id = $(this).attr('id');
				viewLook(id, opts);
				return false;
			});
			//查看链接
			$this.find('.aview1').unbind('click').click(function(){
				var id = $(this).attr('id');
				viewLook1(id, opts);
				return false;
			});
			//分页
			$this.find('#page-bar a').unbind('click').click(function(){
				var pageNo = $(this).attr('value');
				if(Public.isNull(pageNo)){
					pageNo = 1;
				}
				$this.find("#pageNo").val(pageNo);
				$this.searchList(opts);
			});

			//$lastTrClone.hide();
			//return opts;
			this.opts = opts;
			return this;
		},
		searchList: function(opts){
			if(Public.isNull(opts.listMethod)){
				var url = buildUrl( opts.namespace, opts.listPageMethod);
				location.href = url;
			}else{
				var url = buildUrl( opts.namespace, opts.listMethod);
				var $container = opts.container;
				var $form = $('#'+opts.formId);
				var paramString = $form.serialize();
				$container.loadContent2(url, paramString, function(){
					$container.initTable(opts, $container.parent());
				});
			}
		},
		reloadList: function(){
			$(this).searchList(this.opts);
		},
		addEntity: function(opts){//新增
			viewEdit('', opts);
		},
		removeEntity: function(id, opts){
			if( Public.isNull(id) ){
				Public.message('id不能为空');
				return;
			}
			var ids = new Array(id);
			Public.confirm('确定要删除吗？', function(){
				removeEntity(ids, $(this), opts);
			});
		},
		statusEntity: function(id, opts){
			if( Public.isNull(id) ){
				Public.message('id不能为空');
				return;
			}
			var ids = new Array(id);
			Public.confirmStatus('确定阅读该留言吗？', function(){
				statusEntity(ids, $(this), opts);
			});
		},
		tixianEntity: function(id, opts){
			if( Public.isNull(id) ){
				Public.message('id不能为空');
				return;
			}
			var ids = new Array(id);
			Public.confirmStatus('确定处理该提现吗？', function(){
				tixianEntity(ids, $(this), opts);
			});
		},
		removeSeletedEntity: function(opts){
			var selectItems = $(this).getSelected();
			if(selectItems.length < 1){
				Public.message('请选择需要删除的信息');
				return;
			}

			Public.confirm('确定要删除吗？', function(){
				removeEntity(selectItems, $(this), opts);
			});
		},
		selectAll: function(opts){
			opts = $.extend( {
				selectedBgColorClass: 'selectedcolor'
			}, opts);
			$(this).find(':checkbox:first').attr('checked', true);
			selectAll($(this), opts.selectedBgColorClass);
		},
		unselectAll: function(opts){
			opts = $.extend( {
				selectedBgColorClass: 'selectedcolor'
			}, opts);
			$(this).find(':checkbox:first').attr('checked', false);
			unselectAll($(this), opts.selectedBgColorClass);
		},
		getSelected: function(){
			var selectItems = [];
			$(this).find('[name="ids"]:checked').each(function() {
				selectItems.push($(this).val());
			});

			return selectItems;
		}
	});

	//全选
	function selectAll($this, selectedClass){
		$this.find(':checkbox').attr('checked', true);
		$this.find('tr:gt(0)').each(function(i, e){
			if($(e).find(":checkbox").length > 0 ){
				$(e).addClass(selectedClass);
			}
		});
	}
	//全不选
	function unselectAll($this, selectedClass){
		$this.find(':checkbox').attr('checked', false);
		$this.find('tr:gt(0)').each(function(i, e){
			if($(e).find(":checkbox").length > 0 ){
				$(e).removeClass(selectedClass);
			}
		});
	}
	//构造路径
	function buildUrl(namespace, method){
		return namespace + method;
	}

	//打开编辑页面
	function viewEdit(id, opts){
		$(document).unbind('keydown');
		var method;
		if(Public.isNull(id)){//新增
			method = opts.addMethod;
		}else{//修改
			if(Public.isNull(id)){
				alert("id不能为空");
				return;
			}
			method = opts.editMethod;
			method = method.replace(/{id}/g, id);
		}
		var url = buildUrl(opts.namespace, method);

		//data +='&params[pageParam]=' + Public.encode( $('#'+opts.formId).serialize() );
		var $form = $('#'+opts.formId);
		var listUrl = Public.appendQuestionMarkOrAnd( $form.attr("action"), $form.serialize() ),
			createUrl = buildUrl(opts.namespace, opts.addMethod);

		/*createUrl = Public.appendQuestionMarkOrAnd(createUrl,
			"params[listUrl]=" + listUrl + "&params[createUrl]=" + createUrl);*/
		
		var data = 'params[listUrl]=' + Public.encode( listUrl ) +
				'&params[createUrl]=' + Public.encode( createUrl ); 
		
		url = Public.appendQuestionMarkOrAnd(url, data);
		openURL(url);
	}
	
	function openURL(url){
		setTimeout(function(){
			location.href = url;
		}, 0);
	}
	
	function viewLook(id, opts){
		$(document).unbind('keydown');
		var method = opts.viewMethod;
		method = method.replace(/{id}/g, id);
		var url = buildUrl(opts.namespace, method);
		
		var listUrl = Public.appendQuestionMarkOrAnd( 
				buildUrl(opts.namespace, opts.listPageMethod), $('#'+opts.formId).serialize());
		var data = 'params[listUrl]=' + Public.encode( listUrl );
		
		url  = Public.appendQuestionMarkOrAnd( url, data );
		openURL(url);
	}
	
	function viewLook1(id, opts){
		$(document).unbind('keydown');
		var method = opts.view1Method;
		method = method.replace(/{id}/g, id);
		var url = buildUrl(opts.namespace, method);
		
		var listUrl = Public.appendQuestionMarkOrAnd( 
				buildUrl(opts.namespace, opts.listPageMethod), $('#'+opts.formId).serialize());
		var data = 'params[listUrl]=' + Public.encode( listUrl );
		
		url  = Public.appendQuestionMarkOrAnd( url, data );
		openURL(url);
	}
	
	//删除方法
	function removeEntity(ids, $table, opts){
		if(!ids || ids == null){
			Public.message('参数不能为空');
			return;
		}
		var paramString = '_method=delete';
		$(ids).each(function(i){
			paramString += "&"+ opts.checkboxId+"="+ids[i];
		});
		
		var url = buildUrl(opts.namespace, opts.deleteMethod);
		$.ajax({
			url: url,
			type: "POST",
			data: paramString,
			success: function(result) {
				if( 1 == result ){
					result = "删除成功";
					$table.searchList(opts);
				}
				Public.message(result);
			},
			error: function(){
				Public.message("删除失败");
			}
		});
	} 
	//修改已读状态
	function statusEntity(ids, $table, opts){
		if(!ids || ids == null){
			Public.message('参数不能为空');
			return;
		}
		var paramString = '_method=delete';
		$(ids).each(function(i){
			paramString += "&"+ opts.checkboxId+"="+ids[i]+"&type=0";
		});
		
		var url = buildUrl(opts.namespace, opts.deleteMethod);
		$.ajax({
			url: url,
			type: "POST",
			data: paramString,
			success: function(result) {
				Public.message(result);
				$table.searchList(opts);
			},
			error: function(){
			}
		});
	} 
	//修改是否体现状态
	function tixianEntity(ids, $table, opts){
		if(!ids || ids == null){
			Public.message('参数不能为空');
			return;
		}
		var paramString = '_method=delete';
		$(ids).each(function(i){
			paramString += "&"+ opts.checkboxId+"="+ids[i]+"&type=1";
		});
		
		var url = buildUrl(opts.namespace, opts.deleteMethod);
		$.ajax({
			url: url,
			type: "POST",
			data: paramString,
			success: function(result) {
				Public.message(result);
				$table.searchList(opts);
			},
			error: function(){
			}
		});
	} 
})(jQuery);