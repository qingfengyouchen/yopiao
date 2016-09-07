<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>个人中心-在线充值</title>
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
    	<h3>在线充值</h3>
        <div class="back"><a href="javascript:history.go(-1)">返回</a></div>
        <div class="more"><a href="#"></a></div>
    </div>
</div>
<div class="hg50"></div>
<!--头部通用 结束-->
<!--在线充值主体部分 开始-->
<div class="hyzx-zxcz w_100">
	<div class="zxcz-btn mb10 clearfix"><input class="comtxt" type="text" value="" placeholder="请输入充值金额(元)" /></div>
	<div class="order-paytype w_100 mb10">
    	<div class="order-paytype-title">支付方式</div>
        <ul class="paytype-list clearfix">
        	<li class="active">
            <div class="icon"></div>
            <img src="${rootUri}/static/wx/images/order_paytype_zfb.jpg" /> 
            <h3>支付宝</h3>
            <p>推荐支付宝用户使用</p></li>
            <li>
            <div class="icon"></div>
            <img src="${rootUri}/static/wx/images/order_paytype_wx.jpg" /> 
            <h3>微信</h3>
            <p>推荐已安装微信的用户使用</p></li>
        </ul>
        <script>
        	$(".paytype-list li").click(function(){
				$(this).addClass("active").siblings().removeClass("active");
			});
        </script>
    </div>
    <div class="zxcz-foot"><input class="combtn btnbg" type="button" value="确认充值" /></div>
</div>
<!--在线充值主体部分 结束-->

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
