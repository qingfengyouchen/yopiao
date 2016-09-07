/***
 * 扩展插件
 * @param {Object} $
 * @memberOf {TypeName} 
 */
(function($){
	$.extend($.fn, {
		waitMsg: function(msg){
			if(Public.isNull(msg))
				msg = '正在加载，请稍等……';
			
			if($('#div-progress').length < 1){
				var html = '<div id="div-progress">'
					 			+ '<span class="loading" style="padding-left:18px;font-size: 15px;font-weight: bold;">'+msg+'</span>'
					 		+' </div>';
				
				$(this).html(html);
			}else
				$('#div-progress').show();
		},
		loadContent: function(url, data, callback){
			var $this = $(this);
			if($this.length < 1){
				Public.message('请求错误：没有对应的加载容器对象');
				return;
			}
			Public.waitDialogMsg();
			$.ajaxSetup ({ 
				cache: false 
			});
			
			url = Public.buildUrl(url, "jshowProgress=true");
			try{
				$this.load(url, data, function(response, status, xhr) {
					//Public.message(status + "<br>" + xhr.status +"<br>" + xhr.statusText);
					if ( "error" == status) {
						/*$this.html('<span style="font-size: 15px;font-weight: bold; color:red;">'
								+ '加载数据失败</span><a href="'
								+ ctx +'/front/success"><span style="font-size: 15px;padding-left: 10px;">返回首页</span></a>');
						$this.height(300);*/
						/*if( "0" == xhr.status ){
							Public.message("可能由于登陆超时或者断开了与服务器的连接，导致连接失败，请重新登陆或检查网络连接");
						}else */if( "403" == xhr.status ){
							$this.load(ctx + "/common/403.jsp");
						}else if( "404" == xhr.status ){
							$this.load(ctx + "/common/404.jsp");
						}else if( "500" == xhr.status ){
							$this.load(ctx + "/common/500.jsp");
						}else if( "502" == xhr.status ){
							Public.message("网关错误，请检查网络");
						}else if( "504" == xhr.status ){
							Public.message("请求超时，请稍后再试");
						}/*else{
							Public.message("请求失败，请稍后再试");
						}*/
						Public.loaded();
						return false;
					}else if("success" == status){
						if(callback)
							callback();
					}
					Public.loaded();
				});
			}catch(e){
				Public.loaded();
			}
		},
		loadContent2: function(url, data, callback){
			var $this = $(this);
			$this.html("<tr><td colspan='"+$this.find("tr:first>td").length+"'></td></tr>");
			$this.find("td:first").waitMsg();
			
			$.ajaxSetup ({ 
				cache: false 
			});
			$this.load(url, data,function(response, status, xhr) {
				if (status == "error") {
					$this.html('<span style="font-size: 15px;font-weight: bold; color:red;">' + xhr.statusText + '加载失败</font>');
				}else{
					Public.loaded();
					if(callback)
						callback();
				}
				
			});
		},
		submitForm: function(callback, $msg){
			var $this = $(this);
			$this.submit();
			if( !$this.valid() )
				return;
			if(!$msg)
				$msg = $this.parent();
			var $showMsg = Public.waitLoadMsg( $msg, '正在提交，请稍等……', null, 1000);
			var flag = false;
			try{
				if(beforeSubmit){
					flag = beforeSubmit();
				}else
					flag = true;
			}catch(e){flag = true;}
			
			if(!flag)
				return;
				
			var url = $this.attr('action');
			var paramString = $this.serialize();
			
			$.ajax({
				  url: url,
				  type: "POST",
				  data: paramString,
				  dataType:'text',
				  progressObj: $showMsg,
				  success: function(msg) {
					  Public.closeWaitLoadMsg($showMsg);
					  if(Public.isNull(msg)){
							Public.message('保存数据失败');
						}else{
							if(callback)
								callback(msg);
							else
								Public.message(msg);
						}
				  },
				  error: function(){
					  Public.message('请求失败，请稍后再试');
					  Public.closeWaitLoadMsg($showMsg);
				  }
			});
		},
		submitBackForm: function(callback){
			var $this = $(this);
			$this.submit();
			if( !$this.valid() )
				return;
			var $msg = $this.parent();
			var $showMsg = Public.waitLoadMsg( $msg, '正在提交，请稍等……', null, 1000);
			var url = $this.attr('action');
			var paramString = $this.serialize();
			
			$.ajax({
				url: url,
				type: "POST",
				data: paramString,
				success: function(msg) {
					Public.closeWaitLoadMsg($showMsg);
					if(Public.isNull(msg)){
						$.messager.alert('操作提示', '保存数据失败','info');
					}else{
						if(callback){
							callback();
						}else
							$.messager.alert('操作提示', '保存数据成功','info');
					}
				},
				error: function(){
					Public.closeWaitLoadMsg($showMsg);
					$.messager.alert('操作提示', '请求错误，请稍后再试','info');
				}
			});
		},
		resetForm: function(){
			var $this = $(this);
			$this.find(':text').val('');
			$this.find(':file').val('');
			$this.find('textarea').val('');
			$this.find(':radio').attr('checked', false);
			$this.find(':checkbox').attr('checked', false);
			
			setTimeout(function() { 
				$this.find('select').val('');
			}, 1);
		},

		//全选，反选
		bindCheckAll: function($target){
			var $this = $(this);
			if($target){
				$this.find(":checkbox:first").click(function(){
					var isChecked = $(this).attr('checked')=='checked'? true : false;
					$target.find(':checkbox').attr('checked', isChecked);
				});
			}else{
				$this.find(":checkbox:first").click(function(){
					var isChecked = $(this).attr('checked')=='checked'? true : false;
					$this.find(':checkbox:gt(0)').attr('checked', isChecked);
				});
			}
		},
		getCheckedIds: function(name){
			if(Public.isNull(name))
				name = "items";
			var selectItems = [];

			$(this).find(':checkbox[name="'+name+'"][checked]').each(function() {
				if($(this).val() != '')
					selectItems.push($(this).val());
			});

			return selectItems;
		},
		getCheckedAttrs: function(checkboxName, attrName){
			if(Public.isNull(checkboxName))
				checkboxName = "items";
			var selectItems = [];

			$(this).find(':checkbox[name="'+checkboxName+'"][checked]').each(function() {
				if($(this).attr(attrName) != '')
					selectItems.push($(this).attr(attrName));
			});

			return selectItems;
		},
		deleteEnity: function(url){
			var $form = $(this);
			var items = $form.getCheckedIds();
			if (items.length < 1) {
				$.messager.alert('提示信息','请先选择要删除的信息','info');
				return;
			}
			$.messager.confirm('删除信息', '确定要删除吗？', function(r){
				if (r){
					$('input[name="_method"]').remove();
					$form.attr("action", url)
						.append('<input type="hidden" name="_method" value="DELETE" />')
						.attr("method","post")
						.submit();
				}
			});
		},
		deleteEnityAjax: function(url, callback){
			var $form = $(this);
			var items = $form.getCheckedIds();
			if (items.length < 1) {
				$.messager.alert('提示信息','请先选择要删除的信息','info');
				return;
			}
			$.messager.confirm('删除信息', '确定要删除吗？', function(r){
				if (r){
					$.ajax({
						url: url,
						type: "POST",
						data: {_method:'delete', items: items[0]},
						success: function(msg) {
							if(Public.isNull(msg)){
								$.messager.alert('操作提示', '删除数据失败','info');
							}else{
								if(callback){
									callback();
								}else
									$.messager.alert('操作提示', '删除数据成功','info');
							}
						}
					});
				}
			});
		},
		deleteFrontEnityAjax: function(url, callback){
			var $form = $(this);
			var items = $form.getCheckedIds();
			if (items.length < 1) {
				Public.message('请先选择要删除的信息');
				return;
			}
			var data = '_method=delete' + Public.parseIds2ParamStr(items, "items", true);
			Public.confirm('确定要删除吗？', function(r){
				$.ajax({
						url: url,
						type: "POST",
						data: data,
						success: function(msg) {
							if(Public.isNull(msg)){
								Public.message( '删除数据失败');
							}else{
								if(callback){
									callback();
								}else
									Public.message('删除数据成功');
							}
						}
				});
			});
		},
		/**
		 * 绑定div下拉框
		 * @param opts
		 */
		bindDivSelect: function(opts){
			var defaultOpts = {
				oddClass: 'bg-gray', //奇数行样式
				evenClass: '', //偶数行样式
				mouseoverClass: 'bg-yellow', //鼠标移上去的样式
				colWidth: 100,
				onchange: undefined
			};
			
			if(Public.isNull(opts)){
				opts = defaultOpts;
			}else{
				opts =  $.extend(defaultOpts, opts);
			}
			var $this = $(this),
				varName= $this.attr('varName'),
				defaultVal = $this.attr('val');
			
			if(Public.isNull(defaultVal)){
				$this.find('li:first').text($this.find('p:first').text());
				defaultVal = $this.find('p:first').attr('val');
			}else{
				$this.find('li:first').text($this.find('p[val="'+defaultVal+'"]').text());
			}
			if(Public.isNull(defaultVal))
				defaultVal = "";
			if($this.find('input:hidden').length==1){
				$this.find('input:hidden').val(defaultVal);
			}else{
				$this.append('<input type="hidden" name="'+varName+'" value="'+defaultVal+'"/>');
			}
			
			$this.unbind('click').click(function(){
				var disabled = $(this).attr("disabled");
				if( "disabled" == disabled)
					return;
				
				$this.find('div').toggle();
			});
			
			if(!Public.isNull(opts.mouseoverClass)){
				$this.find('p').unbind().bind({
					mouseover: function(){
						$(this).addClass(opts.mouseoverClass);
					},
					mouseout: function(){
						$(this).removeClass(opts.mouseoverClass);
					}
				});
			}
			
			if(!Public.isNull(opts.oddClass)){
				var $options = $this.find('p:odd');
				if(!Public.isNull(opts.evenClass))
					$options.removeClass(opts.evenClass);
				$options.addClass(opts.oddClass);
			}
			
			if(!Public.isNull(opts.evenClass)){
				var $options = $this.find('p:even');
				if(!Public.isNull(opts.oddClass))
					$options.removeClass(opts.oddClass);
				$options.addClass(opts.evenClass);
			}
			
			$this.find('p').unbind('click').click(function(){
				var key = $(this).attr('val'), 
					value = $(this).text();
				key = Public.isNull(key) ? "" : key;
				value = (Public.isNull(value) || value== "&nbsp;") ? "" : value;
				
				var ischange = false;
				if($this.find('input:hidden').length==1){
					if(key == $this.find('input:hidden').val())
						ischange = false;
					else
						ischange = true;
					$this.find('input:hidden').val(key);
				}else{
					ischange = true;
					$this.append('<input type="hidden" name="'+varName+'" value="'+key+'"/>');
				}
				$this.find('li:first').text(value);
				
				if(opts.onchange && ischange){
					opts.onchange();
				}
				Public.loaded();
			});
			
			var flag = false;
			$this.unbind('mouseover').mouseover(function(){
				flag = true;
				/*if($this.find('div').is(':visible')){
					
				}*/
			});
			$this.unbind('mouseout').mouseout(function(){
				flag = false;
				setTimeout(function(){
					if(!flag)
						$this.find('div').hide();
				}, 500);
			});
			
			return opts;
		},
		/***
		 * 
		 * @param key
		 * @param value
		 * @param index
		 */
		appendSelectOption: function(opts, key, value, index){
			var optionHtml = '<p class="float-l ellipsis" style="padding-left: 5px;width: '+ opts.colWidth +'px;" val="'+key+'">'+value+'</p>';
			var $opt = $(this).find('div>ul');
			if( $opt.find('>p').length > 0 ){
				var $opts = $opt.find('>p'),
					maxIndex = $opts.length - 1;
				
				if(index > maxIndex)
					index = maxIndex;
				$opts.eq(index).before(optionHtml);
			}else
				$opt.html(optionHtml);
			
			$(this).bindDivSelect(opts);
			$(this).setDivSelectVal(key);
		},
		/**
		 * 设置div下拉框选项
		 * @param jsonArr json数组
		 * @param key json数组元素的key
		 * @param value json数组元素的value
		 * @param opts
		 * @param isKeepFistOpt是否保留第一项
		 */
		setDivSelectOpts: function(jsonArr, key ,value, opts, isKeepFistOpt){
			if(jsonArr){
				var $this = $(this);
				var $contrainer = $this.find('div > ul');
				if(isKeepFistOpt)
					$contrainer.children(':gt(0)').remove();
				else
					$contrainer.html('');
				var widthStr = "";
				try{
					if(opts.colWidth)
						widthStr = "width: "+ opts.colWidth +"px;";
				}catch(e){}
				
				for(var i = 0; i < jsonArr.length; i++){
					$contrainer.append('<p class="float-l ellipsis" style="padding-left: 5px;'+ widthStr +'" val="'+jsonArr[i][key]+'" title="'+jsonArr[i][value]+'">'+jsonArr[i][value]+'</p>');
				}
				$this.find('li:first').text("");
				$this.find('input:hidden').val("");
				$this.attr('val', "");
				$this.bindDivSelect(opts);
			}
		},
		/**
		 * 设置选中的元素
		 * @param value
		 */
		setDivSelectVal: function(v, isSetFirstAsVal){
			var $this = $(this), key='', value ='';
			if(Public.isNull(v)){
				if(isSetFirstAsVal){
					var $opt = $this.find('p:first');
					key = $opt.attr('val');
					value = $opt.text();
				}
			}else{
				key = v;
				value = $this.find('p[val="'+v+'"]').text();
			}
			$this.find('li:first').text( value );
			$this.find('input:hidden').val( key );
		},
		/**
		 * 设置选中的元素
		 * @param value
		 */
		setDivSelectName: function(name, isSetFirstAsVal){
			var $this = $(this),  key='', value ='';
			if(Public.isNull(name)){
				if(isSetFirstAsVal){
					var $opt = $this.find('p:first');
					key = $opt.attr('val');
					value = $opt.text();
				}
			}else{
				key = $this.find('p[title="'+name+'"]').attr('val');
				value = name;
			}
			$this.find('li:first').text(value);
			$this.find('input:hidden').val( key );
		},
		/**
		 * 获得div下拉框选中的值
		 * @returns
		 */
		getDivSelectVal: function(){
			return $(this).find('input:hidden').val();
		},
		getDivSelectName: function(){
			var name = "";
			try{
				name = $(this).find('p[val='+ $(this).getDivSelectVal() +']').text();
			}catch(e){}
			
			return name;
		},
		chooseCondition: function(opts){
			var defaultOpts = {
				name: '',
				value: '',
				selectedClass: 'yellowbtn', 
				commonClass: 'graybtn',
				overClass: 'orangebtn',
				onchange: undefined
			};
				
			if(Public.isNull(opts)){
				opts = defaultOpts;
			}else{
				opts =  $.extend(defaultOpts, opts);
			}
			var $Contrainer = $(this);
			//绑定事件
			$Contrainer.find(" > div").bind({
				click: function(){
					$public.changeValue($Contrainer, $(this), opts.selectedClass, opts.commonClass, true, opts.onchange);
				}, 
				mouseover: function(){
					$(this).removeClass(opts.commonClass).addClass(opts.overClass);
				},
				mouseout: function(){
					$(this).removeClass();
					
					if($(this).attr('val') == $Contrainer.find(":hidden").val())
						$(this).addClass(opts.selectedClass);
					else
						$(this).addClass(opts.commonClass);
				}
			});
			
			if($Contrainer.find(':hidden').length<1){
				$Contrainer.append('<input type="hidden" name="'+opts.name+'" value="'+opts.value+'"/>');
			}else{
				$Contrainer.find(':hidden').val(opts.value);
			}
			
			var $this;
			if(Public.isNull(opts.value)){
				$this = $Contrainer.find(' > div:first');
				opts.value = $this.attr('val');
			}else
				$this = $Contrainer.find(' > div[val='+opts.value+']');
			$public.changeValue($Contrainer, $this, opts.selectedClass, opts.commonClass);
		}
	});
})(jQuery);