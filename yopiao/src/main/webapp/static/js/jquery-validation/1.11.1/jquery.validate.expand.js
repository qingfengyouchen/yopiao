/*
 * jQuery validation 中国化扩展
 *
 */

// 中文字两个字节     
jQuery.validator.addMethod("byteRange",
		function(value, element, param) {
			var length = value.length;
			for ( var i = 0; i < value.length; i++) {
				if (value.charCodeAt(i) > 127) {
					length++;
				}
			}
			return this.optional(element)
					|| (length >= param[0] && length <= param[1]);
		}, "请确保输入的值在3-15个字节之间(一个中文字算2个字节)");

/* 追加自定义验证方法 */
// 身份证号码验证
jQuery.validator.addMethod("isIdCardNo", function(value, element) {
	return this.optional(element) || isIdCardNo(value);
}, "身份证输入错误");

// 字符验证
jQuery.validator.addMethod("userName", function(value, element) {
	return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
}, "用户名只能包括中文字、英文字母、数字和下划线");

jQuery.validator.addMethod("isSn", function(value, element) {
	return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);

}, "只能包括中文字、英文字母、数字和下划线");

// 中文验证
jQuery.validator.addMethod("isChinese", function(value, element) {
	return this.optional(element) || /^[\u4e00-\u9fa5]+$/.test(value);
}, "用户名只能是中文");

// 网址验证
jQuery.validator.addMethod("isURL", function(value, element) {
	var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
			+ "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
			+ "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
			+ "|" // 允许IP和DOMAIN（域名）
			+ "([0-9a-z_!~*'()-]+\.)*" // 域名- www.
			+ "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名
			+ "[a-z]{2,6})" // first level domain- .com or .museum
			+ "(:[0-9]{1,4})?" // 端口- :80
			+ "((/?)|" // a slash isn't required if there is no file name
			+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
	var re = new RegExp(strRegex);
	return this.optional(element) || re.test(value);
}, "地址格式错误");

// 数字值域验证
jQuery.validator.addMethod("numRange", function(value, element, param) {
	return this.optional(element) || (value >= param[0] && value <= param[1]);
}, "请输入值域内的数值");

// 整数验证
jQuery.validator.addMethod("isNumber", function(value, element) {
	if (value == "")
		return true;
	return this.optional(element) || (/^\d+$/.test(value));
}, "请正确填写整数");
// 小数验证
jQuery.validator.addMethod("isDoubleNumber", function(value, element) {
	if (value == "")
		return true;
	return this.optional(element) || (/^\d+(\.\d{1,2})?$/.test(value));
}, "请正确填写数字");

jQuery.validator.addMethod("isMobile", function(value, element) {
	var length = value.length;
	var mobile = /^\d{11}$/;
	return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写您的手机号码");

// 电话号码验证
jQuery.validator.addMethod("isTel", function(value, element) {
	var tel = /^(\d{3,4}-)?\d{7,9}$/; // 电话号码格式010-12345678
	return this.optional(element) || (tel.test(value));
}, "请正确填写您的电话号码");

// 联系电话(手机/电话皆可)验证
jQuery.validator.addMethod("isPhone", function(value, element) {
	//var length = value.length;
	return this.optional(element) || validateForm.isPhone(value);
}, "请正确填写您的联系电话");
// 邮政编码验证
jQuery.validator.addMethod("isZipCode", function(value, element) {
	if (value == "")
		return true;
	var tel = /^[0-9]{6}$/;
	return this.optional(element) || (tel.test(value));
}, "请正确填写您的邮政编码");

// ip验证
jQuery.validator
		.addMethod(
				"isIp",
				function(value, element) {
					var ip = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
					return this.optional(element) || (ip.test(value));
				}, "请正确填写您的IP地址");

// url验证，已有前缀http://www.
jQuery.validator
		.addMethod(
				"isPrefixURL",
				function(value, element) {
					if ('http://www.' == value)
						return true;
					var url = /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i;
					return this.optional(element) || (url.test(value));
				}, "请正确填写您的URL地址");


//邮箱验证
jQuery.validator
		.addMethod(
				"isEmail",
				function(value, element) {
					return this.optional(element) || (checkEmail(value));
				}, "请正确填写您的邮箱地址");


// 必填其一验证
jQuery.validator.addMethod("requiredGroup", function(value, element) {
	var $module = $(element).parents("div");
	return $module.find(".requiredGroup:filled").length;
}, "必须填写其中一个");

/**
 * 比较两个日期的大小 但开始时间的id的格式必须为beginDate[i],结束时间的id:endDate[@i]
 * []表示可有可无，即可选项,i：表示索引号 当页面只有一个开始时间和结束时间时，命名方法是beginDate,endDate
 * 当页面有多个开始时间和结束时间，命名方法是beginDatei,endDate@
 */
jQuery.validator.addMethod("isCompareDate", function(value, element) {
	var endDate = $(element).attr("id");

	if (beginDate != '' && value != "") {
		endDate = endDate.split("@");
		var beginDate = "beginDate";
		if (endDate.length > 1) {
			beginDate += endDate[1];
		}
		beginDate = $('#' + beginDate).val();
		return new Date(Date.parse(beginDate.replace("-", "/"))) <= new Date(
				Date.parse(value.replace("-", "/")));
	} else {
		return true;
	}
	// var startDate = $('#startDate').val();

}, "结束日期必须大于开始日期!");

$.validator.addMethod("endDate",
        function(jfDate, element) {
           var startDate = $('#beginDate').val();
        return startDate <= jfDate;
        },
        "结束日期必须大于开始日期!"
    );




/*******************************************************************************
 * 验证文件的格式
 * 
 * @param {Object}
 *            value
 * @param {Object}
 *            element
 * @param {Object}
 *            param 格式列表
 * @memberOf {TypeName}
 * @return {TypeName}
 */
jQuery.validator.addMethod("chkFileType", function(value, element, param) {
	return this.optional(element) || checkFileType(value, param);
}, "文件格式错误！");

function checkFileType(filename, allImgExt) {
	var fileExt = filename.substr(filename.lastIndexOf(".") + 1).toLowerCase();
	if (allImgExt.indexOf(fileExt) < 0) {
		return false;
	}
	return true;
}

// 身份证号码验证
function isIdCardNo(idcard) {
	var area = {
		11 : "北京",
		12 : "天津",
		13 : "河北",
		14 : "山西",
		15 : "内蒙古",
		21 : "辽宁",
		22 : "吉林",
		23 : "黑龙江",
		31 : "上海",
		32 : "江苏",
		33 : "浙江",
		34 : "安徽",
		35 : "福建",
		36 : "江西",
		37 : "山东",
		41 : "河南",
		42 : "湖北",
		43 : "湖南",
		44 : "广东",
		45 : "广西",
		46 : "海南",
		50 : "重庆",
		51 : "四川",
		52 : "贵州",
		53 : "云南",
		54 : "西藏",
		61 : "陕西",
		62 : "甘肃",
		63 : "青海",
		64 : "宁夏",
		65 : "新疆",
		71 : "台湾",
		81 : "香港",
		82 : "澳门",
		91 : "国外"
	}
	var idcard, Y, JYM;
	var S, M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	// 地区检验
	if (area[parseInt(idcard.substr(0, 2))] == null)
		return false;// Errors[4];
	// 身份号码位数及格式检验
	switch (idcard.length) {
	case 15:
		if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
				|| ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
						.substr(6, 2)) + 1900) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
		} else {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
		}
		if (ereg.test(idcard) == false)
			return false;// Errors[2];
		break;
	case 18:
		// 18位身份号码检测
		// 出生日期的合法性检查
		// 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
		// 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
		if (parseInt(idcard.substr(6, 4)) % 4 == 0
				|| (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
						.substr(6, 4)) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}[0-9]{4}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
		} else {
			ereg = /^[1-9][0-9]{5}[0-9]{4}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
		}
		if (ereg.test(idcard)) {// 测试出生日期的合法性
			// 计算校验位
			S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
					+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
					* 9
					+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
					* 10
					+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
					* 5
					+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
					* 8
					+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
					* 4
					+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
					* 2 + parseInt(idcard_array[7]) * 1
					+ parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9])
					* 3;
			Y = S % 11;
			M = "F";
			JYM = "10X98765432";
			M = JYM.substr(Y, 1);// 判断校验位
			if (M != idcard_array[17])
				return false;// Errors[3];
		} else
			return false;// Errors[2];
		break;
	default:
		return false;// Errors[1];
		break;
	}
	return true;
}

function checkEmail(email) {
	var emailRegExp = new RegExp(
			"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
	if (!emailRegExp.test(email) || email.indexOf('.') == -1) {
		return false;
	} else {
		return true;
	}
}

function setErrorMsg($msg, msg){
	$msg.addClass('msg-box msg-box-content')
	 	.html('<em class="error">'+msg+'</em>');
}

function setSuccessMsg($msg){
	$msg.removeClass().addClass('msg-box')
	 	.html('<em class="success">&nbsp;</em>');
}


var validateForm={
		isPhone: function(value){
			var mobile = /^\d{11}$/;
			var tel = /^(\d{3,4}-)?\d{7,9}$/;
			
			return tel.test(value) || mobile.test(value);
		}
		
};
