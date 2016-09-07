<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>订单详情-交易成功</title>
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
    	<h3>订单详情</h3>
        <div class="back"><a href="javascript:history.go(-1)">返回</a></div>
        <div class="more"><a href="#"></a></div>
    </div>
</div>
<div class="hg50"></div>
<!--头部通用 结束-->
<!--订单详情主体部分 开始-->
<div class="order-detail w_100">
	<div class="order-detail-top">
    	<h3>交易成功</h3>
        <img src="${rootUri}/static/wx/images/order_detail_icon05.png" />
    </div>
    <div class="order-detail-address mb10">
    	<p><span>13853344343</span>收货人：小苹果</p>
        <p>收货地址：河北省唐山市滦南县倴城镇安平小区 </p>
    </div>
	<div class="order-lastmess mb10">
    	<p>订单编号：123123123123123123</p>
        <p>支付宝交易号：123123123123123123</p>
        <p>创建时间：123123123123123123</p>
        <p>付款时间：123123123123123123</p>
        <p>发货时间：123123123123123123</p>
        <p>成交时间：123123123123123123</p>
    </div>
    <div class="order-detail-ft01" style="border-bottom:none;">
       <div class="r"><a href="#" class="orderbtn1">评价</a></div>
    </div>
</div>
<!--订单详情主体部分 结束-->

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
