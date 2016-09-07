<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>求约-身份认证</title>
<link rel="stylesheet" type="text/css" href="${rootUri}/static/wx/css/base.css">
<link rel="stylesheet" type="text/css" href="${rootUri}/static/wx/css/style.css">
<script src="${rootUri}/static/wx/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${rootUri}/static/wx/js/TouchSlide.1.1.js"></script>
</head>
<body>
<!--加载中...-->
<div class="page-loader">
  <div class="loader">
    Loading...
  </div>
</div>
<script>
$(function(){
	$(".page-loader").remove();
});
</script>
<!--头部通用 开始-->
<div class="headcom w_100">
	<div class="head">
    	<h3>身份认证</h3>
        <div class="back"><a href="javascript:history.go(-1)">返回</a></div>
        <div class="more"><a href="#"></a></div>
    </div>
</div>
<div class="hg50"></div>
<!--头部通用 结束-->
<!--身份认证主体部分 开始-->
<div class="identity w_100">
	<div class="identity-top">您还没有身份认证，请认证</div>
    <div class="identity-infor mb10">
    	<ul class="clearfix">
        	<li><span class="title">个人姓名：</span><input class="comtxt" type="text" value="" placeholder="请输入姓名" /></li>
            <li><span class="title">身份证号：</span><input class="comtxt" type="text" value="" placeholder="请输入身份证号18位号码" /></li>
        </ul>
    </div>
    <div class="identity-pic">
    	<dl class="clearfix">
        	<dt>身份证正反面照片</dt>
            <dd><label><input type="file" class="comfile" /></label></dd>
            <dd><label><input type="file" class="comfile" /></label></dd>
        </dl>
    </div>  
    <div class="order-read">
    	<div class="input"><input class="order-check" type="checkbox" /></div>
        我已阅读并同意<a href="#" class="orange">签订挂票协议</a>
    </div>   
    <script>
    	$(".order-read .input").click(function(){
			$(this).toggleClass("active");
		});
    </script>
    <div class="comfoot-fixed" style="position:relative; background:none; border:none;"><input class="combtn btnbg" type="button" value="提交" onClick="window.location.href='${rootUri}/wx/url?url=authok'" /></div>
       
</div>
<!--身份认证主体部分 结束-->

<script>
//640px 对应6.4rem
//核心代码，每个页面都要有
	(function (doc, win) {
	  var docEl = doc.documentElement,
		resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
		recalc = function () {
		  var clientWidth = docEl.clientWidth;
		  if (!clientWidth) return;
		  docEl.style.fontSize = 100 * (clientWidth / 640) + 'px';
		};

	  if (!doc.addEventListener) return;
	  win.addEventListener(resizeEvt, recalc, false);
	  doc.addEventListener('DOMContentLoaded', recalc, false);
	})(document, window);
</script> 
<script type="text/javascript" src="${rootUri}/static/wx/js/dp.js"></script>
</body>
</html>
