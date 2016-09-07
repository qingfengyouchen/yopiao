/**
 * 公共方法
 * 
 * @author micheal
 */
Public = {
	ctx: "",
	message : function(opts, callbackClose, callbackSure) {//提示信息
		var msg;
		if (typeof (opts) == 'string') {
			msg = opts;
		}
		
		layer.msg(msg, {icon: 0});
	},
	alert : function(opts, callbackClose, callbackSure) {//提示信息
		var msg;
		if (typeof (opts) == 'string') {
			msg = opts;
		}
		
		layer.alert(msg, {title :'系统提示',icon: 0});
	},
	confirmBoolean : function(opts, callback, callback2) {//确认信息
		var msg;
		if (typeof (opts) == 'string') {
			msg = opts;
		}
		
		layer.confirm(msg,{
			icon: 3, 
			title: '系统提示',
			btn: ['是','否']
		}, function(){
			if (callback){
				callback();
			}
			layer.close();
		}, function(){
			if (callback2){
				callback2();
			}
			layer.close();
		});
	},
	confirm : function(opts, callback, callback2) {//删除提示
		var msg;
		if (typeof (opts) == 'string') {
			msg = opts;
		}
		layer.confirm(msg,{
			title: '请确认',
		    shift:6,
		    closeBtn:2 ,
		    icon : 2,
			btn: ['确定','取消']
		}, function(index){
			layer.close(index);
			if (callback){
				callback();
			}

		}, function(index){
			layer.close(index);
			if (callback2){
				callback2();
			}
		});
	},
	confirmStatus : function(opts, callback, callback2) {//删除提示
		var msg;
		if (typeof (opts) == 'string') {
			msg = opts;
		}
		layer.confirm(msg,{
			title: '请确认',
		    shift:6,
		    closeBtn:2 ,
		    icon : 0,
			btn: ['确定','取消']
		}, function(index){
			layer.close(index);
			if (callback){
				callback();
			}

		}, function(index){
			layer.close(index);
			if (callback2){
				callback2();
			}
		});
	},
	hasLoaded: true,
	waitDialogMsg : function(msg, width, isClickToClose) {//弹出加载提示信息
		Public.hasLoaded = false;
		if (Public.isNull(msg))
			msg = '正在加载，请稍等……';
		
		width = parseInt(width);
		if (isNaN(width))
			width = 200;
		
		if ($('#div-progress').length < 1) {
			var html = '<div id="div-progress" style="display:none;">'
					+ '<div class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-draggable ui-resizable"'
					+ 'style="font-size: 14px;font-weight: bold; height: auto;'
					+ 'position: absolute; z-index: 10003; padding: 13px; width: '+ width +'px;">'
					+ '<span class="loading" style="padding-left:18px;"></span>'//提醒信息
					+ ' </div>'
					+ ' <div class="ui-widget-overlay" style="z-index: 10002; position: absolute;top: 0; left: 0; width: 100%; height: '
					+  $(document).height() +'px;"></div>'
					+ '</div>';

			var $this = $(html);
			$('body').append($this);
			var pageParam = Public.getPageSize();
			var $progress = $("#div-progress div:first");
			$progress.css({
				"left" : (pageParam.pageWidth / 2 - (parseInt($progress.width()) / 2)) + "px",
				"top" : (pageParam.windowHeight / 2 - (parseInt($progress.height()) / 2)) + "px"
			});
		} 
		$('#div-progress span').text(msg);
		
		if(isClickToClose == false){
			$('#div-progress').unbind('click').click(function(){
				Public.loaded();
			});
		}
		
		setTimeout(function(){
			if(!Public.hasLoaded)
				$('#div-progress').show();
		}, 1000);
	},
	loaded : function() {//关闭加载提示信息
		$('#div-progress').hide(800);
		$('#div-progress').remove();
		Public.hasLoaded = true; 
	},
	waitLoadMsg : function($contrainer, msg, width, ms){
		if(!ms)
			ms = 3000;
		
		if (Public.isNull(msg))
			msg = '正在加载，请稍等……';

		width = parseInt(width);
		if (isNaN(width))
			width = 220;
		
		var html = '<div style="display:none">'
						+ '<div class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-draggable ui-resizable"'
						+ 'style="font-size: 14px;font-weight: bold; height: auto;'
						+ 'position: absolute; z-index: 50001; padding: 13px;">'
						+ '<span class="loading" style="padding-left:18px;"></span>'//提醒信息
						+ ' </div>'
						+ ' <div class="ui-widget-overlay" style="z-index: 50000; position: absolute;"></div>'
					+ '</div>';

		var $waitmsg = $(html);
		$contrainer.before($waitmsg);
		setTimeout(function(){
			if($waitmsg.length<1)
				return;
			
			var offset =$contrainer.offset();
			var contrainerW = $contrainer.width(),
				contrainerH = $contrainer.height(),
				y = offset.top,
				x = offset.left;
			
			$waitmsg.find(">div:last").css({
				width: contrainerW +"px",
				height: contrainerH +"px",
				top: y +"px",
				left: x +"px"
			});
			
			var $progress = $waitmsg.find(" > div:first");
			$waitmsg.find('span').text(msg);
			
			$progress.css({
				width: width,
				left : ( contrainerW / 2 - (width / 2) + x ) + "px",
				top : ( contrainerH / 2 - (parseInt($progress.height()) / 2) + y ) + "px"
			});
			
			$waitmsg.show();
		}, ms);
		
		return $waitmsg;
	},
	closeWaitLoadMsg: function($waitmsg){
		try{
			if($waitmsg.length>0)
				$waitmsg.remove();
		}catch(e){}
	},
	closeDialog: function($dialog){
		if($dialog){
			$dialog.dialog("destroy");
			$dialog.remove();
		}
	},
	isNull : function(str) {//判断字符串是否为空
		if (str == '' || str == undefined || str == 'undefined'
				|| str == null || str == 'null' || str=='NULL')
			return true;
		return false;
	},
	isNotNull : function(str){
		return !Public.isNull(str);
	},
	isNumber :function(value) {
        return /(0|^[1-9]\d*$)/.test( value );
    },
	isDoubleNumber: function(value){
		return value == "" || /^\d+(\.\d{1,2})?$/.test(value);
	},
	numRang: function(value, min, max){
		return value == "" ||  (value >= min && value <= max);
	},
	isWebEditorOverFlow: function(){
		if($('td[id$=wordcount] span').length)
			return true;
		return false;
	},
	getPageSize: function(){//获得当前窗口的大小
		var xScroll, yScroll;
		if (window.innerHeight && window.scrollMaxY) {
			xScroll = window.innerWidth + window.scrollMaxX;
			yScroll = window.innerHeight + window.scrollMaxY;
		} else if (document.body.scrollHeight > document.body.offsetHeight) { // all
																				// but
																				// Explorer
																				// Mac
			xScroll = document.body.scrollWidth;
			yScroll = document.body.scrollHeight;
		} else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla
					// and Safari
			xScroll = document.body.offsetWidth;
			yScroll = document.body.offsetHeight;
		}
		var windowWidth, windowHeight;
		if (self.innerHeight) { // all except Explorer
			if (document.documentElement.clientWidth) {
				windowWidth = document.documentElement.clientWidth;
			} else {
				windowWidth = self.innerWidth;
			}
			windowHeight = self.innerHeight;
		} else if (document.documentElement
				&& document.documentElement.clientHeight) { // Explorer 6 Strict
															// Mode
			windowWidth = document.documentElement.clientWidth;
			windowHeight = document.documentElement.clientHeight;
		} else if (document.body) { // other Explorers
			windowWidth = document.body.clientWidth;
			windowHeight = document.body.clientHeight;
		}
		// for small pages with total height less then height of the viewport
		if (yScroll < windowHeight) {
			pageHeight = windowHeight;
		} else {
			pageHeight = yScroll;
		}
		// for small pages with total width less then width of the viewport
		if (xScroll < windowWidth) {
			pageWidth = xScroll;
		} else {
			pageWidth = windowWidth;
		}
		// arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight);
		return {
			"pageWidth" : pageWidth,
			"pageHeight" : pageHeight,
			"windowWidth" : windowWidth,
			"windowHeight" : windowHeight
		};
	},
	/***
	 * 把undefined变量设为指定的值
	 * @param val
	 * @param defaultVal 默认值
	 * @returns
	 */
	setUndefinedVal: function(val, defaultVal){
		if(val == undefined){
			if(Public.isNull(defaultVal))
				val = "";
			else
				val = defaultVal;
		}
		return val;
	},
	//编辑模式
	toEditModel: function(){
		var $params = $('.edit_view');
		$params.each(function(i){
			var $father = $params.eq(i);
			var value = $father.text();
			value = value.replace(/&nbsp;/g, "");
			if(!Public.isNull(value))
				value = value.trim();
					
			var type = $father.attr('type');
			var name = $father.attr('attrName');
			var id = Public.setUndefinedVal($father.attr('attrId'), name);
			var otherAttr = Public.setUndefinedVal($father.attr('otherAttr'));
			
			if(Public.isNull(type) || "input" == type){
				$father.html("<li class='inputbg'><input id='"+id+"' name='"+name+"' value='"+value+"' "+otherAttr+" /></li>");
			}else if("textarea" == type){
				$father.html("<textarea id='"+id+"' name='"+name+"' value='"+value+"' "+otherAttr+">"+value+"</textarea>");
			}
		});
	},
	//查看模式
	toViewModel: function(){
		var $params = $('.edit_view');
		$params.each(function(i){
			var $father = $params.eq(i);
			var $e = $father.find("li").children(":first");
			if($e.length<1)
				$e =  $father.find('textarea');
			var value = $e.val();
			$father.html(value);
		});
	},
	/**
	 * 格式时间
	 * @param value：Long time
	 * @param pattern：格式
	 */
	formatDate: function(value, pattern) {
		if(!Public.isNull(value)){
			var date = new Date();
			date.setTime(value);
			if(Public.isNull(pattern))
				pattern = 'yyyy-MM-dd';
			
			try{
				return date.format(pattern);
			}catch(e){};
		}
		return "";
	},
	/**四舍五入，保留位数为roundDigit ,供计算时用*/
	formatPercent: function(value, roundDigit) {
		if (!Public.isNull(value)) {
			if (value > 0) {
				var tempNumber = parseInt((value * Math.pow(10,roundDigit)+0.5))/Math.pow(10,roundDigit);
				return tempNumber;
			}
			else {
				var tempNumber = parseInt((-value * Math.pow(10,roundDigit)+0.5))/Math.pow(10,roundDigit);
				return -tempNumber;
			}
		}
		return 0;
	},
	selectText: function (textBox,startIndex,endIndex){/*文本框,起始位置,结束位置*/
		if(textBox.setSelectionRange){/*ff,chrome,safari,opera都支持这个方法,只有ie不支持，但它提供了另外的方法实现相同的功能*/
			textBox.setSelectionRange(startIndex,endIndex);
		}else if(textBox.createTextRange){/*ie的实现方法*/
			var range=textBox.createTextRange();
			range.collapse(true);
			range.moveStart("character",startIndex);
			range.moveEnd("character",endIndex-startIndex);
			range.select();
		}
		textBox.focus();
	},
	parseIds2ParamStr: function(ids, name, hasChar){
		var str = "";
		if(ids){
			for(var i=0; i<ids.length; i++){
				str += "&" + name + "=" + ids[i];
			}
			if( !Public.isNull(str) && !hasChar){
				str = str.substring(1);
			}
		}
		
		return str;
	},
	encode: function(a){//ec
	
		var b = "";
		var c = Public.string2bytes(a);	// 经UTF-8转换过来的byte数组
		var d = 0, e = 0, f = 0;

		for (var g = 0; g < c.length; g += 3)
		{
			for (var h = 0; h < 3; h++)
			{
				if (c.length > (g + h))
					e |= (c[g + h] & 0xFF);
				else f++;

				e <<= 8;
			}

			for (var h = 0; h < 4; h++)
			{
				d = (((e & 0xFC000000) >> 26) & 0x0000003F);
				e <<= 6;

				b += vo[d];
			}
		}

		b = b.substr(0, b.length - f);

		return b;
	},

	decode: function(a){//dc
	
		var charLen = Math.ceil(a.length / 4);

		var chars = new Array(charLen * 4);
		var bytes = new Array(charLen * 3);
		var cur = 0;

		for (var i = 0; i < a.length; i++)
			chars[i] = a.charCodeAt(i);
		for (var i = a.length; i < chars.length; i++)
			chars[i] = 0;

		for (var i = 0; i < chars.length; i += 4)
		{
			var b1st = mA[chars[i]];
			var b2nd = mA[chars[i + 1]];
			var b3rd = mA[chars[i + 2]];
			var b4th = mA[chars[i + 3]];

			bytes[cur] = b1st;
			bytes[cur] <<= 2;
			bytes[cur] |= (b2nd & 0x30) >> 4;
			bytes[cur] &= 0xFF;
			cur++;

			bytes[cur] = b2nd;
			bytes[cur] <<= 4;
			bytes[cur] |= (b3rd & 0x3C) >> 2;
			bytes[cur] &= 0xFF;
			cur++;

			bytes[cur] = b3rd;
			bytes[cur] <<= 6;
			bytes[cur] |= (b4th & 0x3F);
			bytes[cur] &= 0xFF;
			cur++;
		}

		var rm = chars.length - a.length;
		for (var i = 0; i < rm; i++) bytes.pop();

		return Public.bytes2string(bytes);
	},


	/**
	 * string2bytes
	 *
	 * @param a		要转换成bytes的字符串
	 * @return		返回的byte的数组
	 */
	string2bytes: function(a){//kw
		var b = [];	// 最终结果的数组
		var c = [];	// 临时存放单个字的byte数组

		for (var d = 0; d < a.length; d++)
		{
			var e = a.charCodeAt(d);
			var f = 0;	// 0表示当前字符是否超过ascii的限制（即需要采用多byte）

			while (e != 0)
			{
				// 未超过ascii的限制
				if (f == 0 && (e | 0x7F) == 0x7F)
				{
					b.push(e);
					break;
				}

				// 已经超过ascii的限制
				f++;
				var g = Public.se(8 - f - 1);

				// 当前值超过6位二进制
				if ((e | 0x3F) > 0x3F)
					c.push((e & 0x3F) | 0x80);
				else if ((e | g) > g)
				{
					c.push((e & 0x3F) | 0x80);
					e >>= 6;

					if (e == 0)
					{
						f++;
						c.push(Public.lw(f));
					}
				}
				else
					c.push(Public.lw(f) | e);

				e >>= 6;
			}

			while (c.length > 0)
				b.push(c.pop());
		}

		return b;
	},

	/**
	 * 一个字节中，计算有a位二进制（全为1）时的值，低位
	 */
	se: function(a)
	{
		return Math.pow(2, a) - 1;
	},

	/**
	 * 一个字节中，计算有a位二进制（全为1）时的值，高位
	 */
	lw: function(a)
	{
		return Math.pow(2, 8) - Math.pow(2, 8 - a);
	},

	/**
	 * bytes2string
	 *
	 * @param a		要转换成string的byte数组
	 * @return		转换好的string
	 */
	bytes2string: function(a){//wk
		var b = "";

		for (var i = 0; i < a.length; i++)
		{
			var c = 0;

			if ((a[i] & 0x80) == 0x00)
				b += String.fromCharCode(a[i] & 0xFF);
			else if ((a[i] & 0xE0) == 0xC0)
			{
				c |= a[i] & 0x1F;

				c <<= 6;
				c |= a[++i] & 0x3F;

				b += String.fromCharCode(c);
			}
			else if ((a[i] & 0xF0) == 0xE0)
			{
				c |= a[i] & 0x0F;

				c <<= 6;
				c |= a[++i] & 0x3F;

				c <<= 6;
				c |= a[++i] & 0x3F;

				b += String.fromCharCode(c);
			}
		}

		return b;
	},


	/**
	 * 收集当前的url，并编码
	 *
	 * @param args	需要插入的参数
	 */
	encodeCurUrl: function(args){//cu
		var encUrl = window["_encoded_url"];

		if (encUrl == null)
		{
			var url = location.pathname + location.search;

			for (var i = 0; args != null && i < args.length; i++)
			{
				var re = new RegExp("(\\?|&)" + args[i]["name"] + "=","g");

				if (!re.test(url))
				{
					url += (url.indexOf("?") >= 0) ? "&" : "?";
					url += args[i]["name"] + "=" + args[i]["value"];
				}
			}

			encUrl = WUrlBase64Coder.encode(url);
			window["_encoded_url"] = encUrl;
		}

		return encUrl;
	},
	/**
	 * 阻止事件冒泡
	 * @param e
	 */
	stopBubble: function(e) {
		 //如果提供了事件对象，则这是一个非IE浏览器
		 if ( e && e.stopPropagation )
	        e.stopPropagation();//因此它支持W3C的stopPropagation()方法
	     else
	        window.event.cancelBubble = true;//否则，我们需要使用IE的方式来取消事件冒泡
	 },
	 
	 /***
	  * 设置图片等比显示
	  * @param $this img jQuery对象
	  * @param maxWidth 设置的最大宽度 当为0时，表示以maxHeight来等比缩放
	  * @param maxHeight 设置的最大高度 当为0时，表示以maxWidth来等比缩放
	  * @param isEnlargeWhenSmall 当图片实际大小小于规定的大小时，是否放大到规定大小
	  */
	 limitImgSize : function($this, maxWidth, maxHeight, isEnlargeWhenSmall) {
		var width = $this.width(); // 图片实际宽度
		var height = $this.height(); // 图片实际高度
		if (isEnlargeWhenSmall || width > maxWidth || height > maxHeight) {// 现有图片只有宽或高超了预设值就进行js控制
			var w = width / maxWidth;
			var h = height / maxHeight;
			if (w > h || maxHeight == 0) {// 比值比较大==>宽比高大
				$this.width(maxWidth).height(height / w);
			} else if( w <= h ||maxWidth==0){// 高比宽大
				$this.width(width / h).height(maxHeight);
			}
		}
	},
	json2str: function (o) {
		var arr = [];
		var fmt = function(s) {
			if (typeof s == 'object' && s != null)
				return Public.json2str(s);

			return /^(string|number)$/.test(typeof s) ? "'" + s + "'" : s;
		};
		for ( var i in o)
			arr.push("'" + i + "':" + fmt(o[i]));

		return '{' + arr.join(',') + '}';
	},
	appendQuestionMarkOrAnd : function(url, paramStr){
		if(url.indexOf("?") < 0){
			url += "?" + paramStr;
		}else{
			var arr = paramStr.split('&');
			for(var i = 0; i < arr.length; i++){
				var paramName = arr[i].split('=')[0];
				if(url.indexOf(paramName)<0){
					url += "&" + arr[i];
				}
			}
		}
		return url;
	},
	/**
	 * 拼接url  
	 */
	buildUrl: function(url, paramStr){
		return url + (url.indexOf("?") < 0 ? "?" : "&") + paramStr; 
	},
	getGUID:function(){
		var guid = "";
		for (var i = 1; i <= 32; i++){
		  var n = Math.floor(Math.random()*16.0).toString(16);
		  guid +=   n;
		  if((i==8)||(i==12)||(i==16)||(i==20))
			guid += "-";
		}
		return guid;    
	},
	getGUID2:function(len){
		var guid = "";
		for (var i = 1; i <= len; i++){
		  var n = Math.floor(Math.random()*16.0).toString(16);
		  guid +=   n;
		}
		return guid;    
	},
	getTimeUID: function(){
		var now = new Date();
		return now.getTime() + "" + Public.getGUID2(4);
	},
	str2json: function(jsonStr){
		if(Public.isNull(jsonStr))
			return null;
		return eval("(" + jsonStr + ")");
	},
	setCenter: function($obj, width, height){
		if($obj){
			$obj.css('position', 'absolute');
			var pageParam = Public.getPageSize();
			if( !width )
				width = $obj.width();
			
			if( !height )
				height = $obj.height();
			
			var position = "fixed";
			if (Public.isIE6()) {
				position = "absolute";
			}
			
			$obj.css({
				left: (pageParam.windowWidth - width)/2 +"px",
				top: (pageParam.windowHeight - height)/2 +"px",
				position: position,
				_position: "absolute"
			});
		}
	},
	setFullScreenShow4IE6: function($this){
		if( !Public.isIE6())
			return;
	
		$this.css({
			width:  $(document).width() - 25,
			height: $(document).height() 
		});
	},
	isIE6: function(){
		return 'undefined' == typeof(document.body.style.maxHeight);//($.browser.msie && $.browser.version == "6.0");
	},
	getIEVersion: function(){
		var v = parseInt($.browser.version);
		if(isNaN(v))
			return -1;
		else
			return v;
	},
	recordMark: function(mark, json){
		var jsonStr = JSON.stringify(json);
		if (window.sessionStorage) {
			try{
				window.sessionStorage.setItem(mark, jsonStr);
			 } catch (e){
			      if (e == QUOTA_EXCEEDED_ERR) {
			    	  window.sessionStorage.removeItem( window.sessionStorage.key(0) );
			    	  Public.recordMark(mark, json);
			      }
			 }
		}else{
			$.ajax({
				url: ctx +'/sys/browser-record/record',
				data:{hashCode: mark, value: jsonStr},
				type: "post",
				dataType: "text",
				async: false,
				success: function(result){
					if("time out" == result)
						location.href = ctx + "/front/login";
				}
			});
		}
	},
	getHashJson: function(hash, callback){
		if( !Public.isNull(hash) ){
			if (window.sessionStorage) {
				if(callback)
					callback(window.sessionStorage.getItem(hash));
			}else{
				$.ajax({
					url: ctx +'/sys/browser-record/get',
					data:{hashCode: hash},
					type: "get",
					dataType: "text",
					async: false,
					success: function(result){
						if(callback)
							callback(result);
					}
				});
			}
		}
	},
	/**
	 * 在指定范围内
	 * 产生[1, maxNum]范围的随机数
	 * @param maxNum
	 * @returns {number}
	 */
	getRandomNum: function(maxNum){
		return Math.ceil(Math.random() * maxNum);
	}
};

var vo  = [ '*', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' ];

var mA  = [ 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19,
			0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0x20, 0x21, 0x22, 0x23, 0x24, 0x00, 0x00, 0x00, 0x00, 0x25,
			0x00, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x34,
			0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F, 0x00, 0x00, 0x00, 0x00, 0x00 ];


/*
 * 添加时间对象的格式化方法
 */
Date.prototype.format = function(format) {
	/*
	 * format="yyyy-MM-dd HH:mm:ss";
	 */
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"H+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};

function formatMonthDate(num) {
	return (num < 10 ? "0" : "") + num;
}
/**
 * 浮动框方法
 */
function liveTip(){
	var tipTitle = '';
	var $liveTip = $('<div id="livetip"></div>').hide().appendTo('body');
	$("[name='imgTip']").bind('mouseover mouseout mousemove', function(event) {
		var $link = $(event.target);
		if (!$link.length) { return; }
		var link = $link[0];
		
		if (event.type == 'mouseover' || event.type == 'mousemove') {
			$liveTip.css({
				top: event.pageY + 12,
				left: event.pageX + 20
			});
		};
		if (event.type == 'mouseover') {
			tipTitle = link.title;
			link.title = '';
			$liveTip.html('<div>' + tipTitle + '</div>').show();
		};
		if (event.type == 'mouseout') {
			$liveTip.hide();
			if (tipTitle) {
				link.title = tipTitle;
			};
		};
	});
}