<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>个人中心</title>
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
    	<h3>个人中心</h3>
        <div class="back"><a href="javascript:history.go(-1)">返回</a></div>
        <div class="more"><a href="#"></a></div>
    </div>
</div>
<div class="hg50"></div>
<!--头部通用 结束-->
<!--个人中心主体部分 开始-->
<div class="hyzx w_100 clearfix">
	<div class="hyzx-top">
    	<div class="hyzx-top-info"><img src="${rootUri}/static/wx/images/ych_detail_place_img01.jpg" />小苹果</div>
        <img src="${rootUri}/static/wx/images/hyzx_banner.jpg" />
    </div>
    <div class="hyzx-list01 mb10">
    	<ul class="clearfix">
        	<li><h3>75</h3><p>我的粉丝</p></li>
            <li><h3>75</h3><p>我的关注</p></li>
            <li><h3>75</h3><p>我的发布</p></li>
            <li><h3>75</h3><p>我的余额</p></li>
        </ul>
    </div>
    <div class="hyzx-list02">
    	<ul>
        	<li class="icon01" onClick="window.location.href='${rootUri}/wx/url?url=wxorders'">全部订单<span class="right">查看全部商品</span></li>
            <li class="icon02" onClick="window.location.href='${rootUri}/wx/url?url=receiveaddr'">地址管理</li>
            <li class="icon03" onClick="window.location.href='${rootUri}/wx/url?url=wxuserinfo'">个人资料</li>
            <li class="icon04" onClick="window.location.href='${rootUri}/wx/url?url=wxnrefunds'">退款/售后</li>
            <li class="icon05" onClick="window.location.href='${rootUri}/wx/url?url=withdraw'">提现申请</li>
            <li class="icon06" onClick="window.location.href='${rootUri}/wx/url?url=wxpassword'">密码修改</li>
            <li class="icon08 mb10" onClick="window.location.href='${rootUri}/wx/url?url=wxrecharge'">在线充值</li>
            <li class="icon07" onClick="window.location.href='${rootUri}/wx/url?url=authentication'">我是商家<span class="right">&nbsp;</span></li>
        </ul>
    </div>
</div>
<div class="hg71"></div>
<div class="hyzx-nav">
	<ul class="clearfix">
    	<li class="active"><a href="#"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon01.png" /></i><p>首页</p></a></li>
        <li><a href="${rootUri}/wx/url?url=friends"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon02.png" /></i><p>好友</p></a></li>
        <li><a href="${rootUri}/wx/url?url=publishmsg"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon03.png" /></i><p>发布</p></a></li>
        <li><a href="${rootUri}/wx/url?url=noticemsg"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon04.png" /></i><p>提醒</p></a></li>
        <li><a href="${rootUri}/wx/url?url=wxhome"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon05.png" /></i><p>我</p></a></li>
    </ul>
</div>
<!--个人中心主体部分 结束-->

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
