<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>订单详情-待收货</title>
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
    	<h3>买家已付款<br>等待买家收货</h3>
        <img src="${rootUri}/static/wx/images/order_detail_icon04.png" />
    </div>
    <div class="order-detail-address mb10">
    	<p><span>13853344343</span>收货人：小苹果</p>
        <p>收货地址：河北省唐山市滦南县倴城镇安平小区 </p>
    </div>
    <div class="order-infor">
        <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
        <h3>容主儿世界巡回演唱会容主儿世界巡回演唱会</h3>
        <p>2016-07-23 19:00</p>
        <p>乐事体育生态中心</p>
    </div>
    <div class="order-detail-ft01">
       <div class="l"><span style="margin-right:10px;">1张</span>合计：<span class="orange">1500</span>元</div>
       <div class="r"><a href="#" class="orderbtn1">确认收货</a></div>
    </div>
    <!--物流开始-->
    <div class="order-detail-wl">
    	<div class="order-detail-wl-top"><span>物流信息：中通快递</span><span>运单号：1231232113</span></div>
        <div class="order-detail-wl-main">
        	<!--当前-->
        	<div class="wllist-item active">
            	<div class="icon"></div>
                <div class="content">
                	<p>[唐山市]河北省某某是快件已签收，签收人：某某某，感谢您使用中通快递！</p>
                    <p>2016-06-06 19:12:21</p>
                </div>
            </div>
            <!--以前-->
            <div class="wllist-item">
            	<div class="icon"></div>
                <div class="content">
                	<p>[唐山市]河北省某某是快件已签收，签收人：某某某，感谢您使用中通快递！</p>
                    <p>2016-06-06 19:12:21</p>
                </div>
            </div>
            <div class="wllist-item">
            	<div class="icon"></div>
                <div class="content">
                	<p>[唐山市]河北省某某是快件已签收，签收人：某某某，感谢您使用中通快递！</p>
                    <p>2016-06-06 19:12:21</p>
                </div>
            </div>
            <div class="wllist-item">
            	<div class="icon"></div>
                <div class="content">
                	<p>[唐山市]河北省某某是快件已签收，签收人：某某某，感谢您使用中通快递！</p>
                    <p>2016-06-06 19:12:21</p>
                </div>
            </div>
            <div class="wllist-item">
            	<div class="icon"></div>
                <div class="content">
                	<p>[唐山市]河北省某某是快件已签收，签收人：某某某，感谢您使用中通快递！</p>
                    <p>2016-06-06 19:12:21</p>
                </div>
            </div>
            
            
        </div>
        <!--物流结束-->
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
