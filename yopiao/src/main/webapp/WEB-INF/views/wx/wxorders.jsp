<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>个人中心-订单管理</title>
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
    	<h3>订单管理</h3>
        <div class="back"><a href="javascript:history.go(-1)">返回</a></div>
        <div class="more"><a href="#"></a></div>
    </div>
</div>
<div class="hg50"></div>
<!--头部通用 结束-->
<!--订单管理主体部分 开始-->
<div class="hyzx-order w_100" id="hyzx-order-slide">
    <div class="hyzx-order-nav hd">
        <ul class="clearfix">
            <li>待付款</li>
            <li>待发货</li>
            <li>待收货</li>
            <li>已完成</li>
        </ul>
    </div>
    <div class="hg50"></div>

    <div class="hyzx-order-mainbox bd">
    	<!--待付款-->
    	<div class="hyzx-order-item">
        	<!--1-->
        	<div class="hyzx-order-comlist">
                <div class="order-infor">
                    <a href="${rootUri}/wx/url?url=wxpaying">
                        <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
                        <h3>容主儿世界巡回演唱会容主儿世界巡回演唱会</h3>
                    </a>
                    <p>2016-07-23 19:00</p>
                    <p><a href="${rootUri}/wx/url?url=venues">乐事体育生态中心</a></p>
                </div>
                <div class="order-ft">
                    <div class="l">1张<span class="orange">1500</span></div>
                    <div class="r"><a href="#" class="orderbtn1">付款</a></div>
                </div>
            </div> 
            <!--1 end-->
            <!--1-->
        	<div class="hyzx-order-comlist">
                <div class="order-infor">
                    <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
                    <h3>容主儿世界巡回演唱会容主儿世界巡回演唱会</h3>
                    <p>2016-07-23 19:00</p>
                    <p><a href="${rootUri}/wx/url?url=venues">乐事体育生态中心</a></p>
                </div>
                <div class="order-ft">
                    <div class="l">1张<span class="orange">1500</span></div>
                    <div class="r"><span class="greyer">票已过期</span></div>
                </div>
            </div> 
            <!--1 end--> 
             
        </div>
        <!--待发货-->
    	<div class="hyzx-order-item">
        	<!--1-->
        	<div class="hyzx-order-comlist">
                <div class="order-infor">
                    <a href="${rootUri}/wx/url?url=wxdelivering">
                        <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
                        <h3>容主儿世界巡回演唱会容主儿世界巡回演唱会</h3>
                    </a>
                    <p>2016-07-23 19:00</p>
                    <p><a href="${rootUri}/wx/url?url=venues">乐事体育生态中心</a></p>
                </div>
                <div class="order-ft">
                    <div class="l">1张<span class="orange">1500</span></div>
                    <div class="r"><span>等待卖家发货</span></div>
                </div>
            </div> 
            <!--1 end-->
            <!--1-->
        	<div class="hyzx-order-comlist">
                <div class="order-infor">
                    <a href="${rootUri}/wx/url?url=wxdelivering">
                        <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
                        <h3>容主儿世界巡回演唱会容主儿世界巡回演唱会</h3>
                    </a>
                    <p>2016-07-23 19:00</p>
                    <p><a href="${rootUri}/wx/url?url=venues">乐事体育生态中心</a></p>
                </div>
                <div class="order-ft">
                    <div class="l">1张<span class="orange">1500</span></div>
                    <div class="r"><span>等待卖家发货</span></div>
                </div>
            </div> 
            <!--1 end--> 
        </div>
        <!--待收货-->
        <div class="hyzx-order-item">
            <!--1-->
            <div class="hyzx-order-comlist">
                <div class="order-infor">
                    <a href="${rootUri}/wx/url?url=wxreceiving">
                        <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
                        <h3>容主儿世界巡回演唱会容主儿世界巡回演唱会</h3>
                    </a>
                    <p>2016-07-23 19:00</p>
                    <p><a href="${rootUri}/wx/url?url=venues">乐事体育生态中心</a></p>
                </div>
                <div class="order-ft">
                    <div class="l">1张<span class="orange">1500</span></div>
                    <div class="r"><a href="#" class="orderbtn2">查看物流</a><a href="#" class="orderbtn1">确认收货</a></div>
                </div>
            </div> 
            <!--1 end-->
            <!--1-->
            <div class="hyzx-order-comlist">
                <div class="order-infor">
                    <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
                    <a href="${rootUri}/wx/url?url=wxreceiving">
                        <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
                        <h3>容主儿世界巡回演唱会容主儿世界巡回演唱会</h3>
                    </a>
                    <p>2016-07-23 19:00</p>
                    <p><a href="${rootUri}/wx/url?url=venues">乐事体育生态中心</a></p>
                </div>
                <div class="order-ft">
                    <div class="l">1张<span class="orange">1500</span></div>
                    <div class="r"><a href="#" class="orderbtn2">查看物流</a><a href="#" class="orderbtn1">确认收货</a></div>
                </div>
            </div> 
            <!--1 end--> 
        </div>
        <!--已完成-->
    	<div class="hyzx-order-item">
        	<!--1-->
        	<div class="hyzx-order-comlist">
                <div class="order-infor">
                    <a href="${rootUri}/wx/url?url=wxdealok">
                        <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
                        <h3>容主儿世界巡回演唱会容主儿世界巡回演唱会</h3>
                    </a>
                    <p>2016-07-23 19:00</p>
                    <p>乐事体育生态中心</p>
                </div>
                <div class="order-ft">
                    <div class="l">1张<span class="orange">1500</span></div>
                    <div class="r"><span>已完成</span><a href="#" class="orderbtn1">去评价</a></div>
                </div>
            </div> 
            <!--1 end--> 
            <!--1-->
        	<div class="hyzx-order-comlist">
                <div class="order-infor">
                    <img src="${rootUri}/static/wx/images/nry_tc_img01.jpg">
                    <h3>容主儿世界巡回演唱会容主儿世界巡回演唱会</h3>
                    <p>2016-07-23 19:00</p>
                    <p>乐事体育生态中心</p>
                </div>
                <div class="order-ft">
                    <div class="l">1张<span class="orange">1500</span></div>
                    <div class="r"><span>已完成</span><span>已评价</span></div>
                </div>
            </div> 
            <!--1 end--> 
        	<!--1-->
        	<div class="order-empty">
            	<div class="img"><img src="${rootUri}/static/wx/images/order_empty_icon.png" /></div>
                <h3>您没有相关订单</h3>
            </div>
            <!--1 end-->
        </div>
    </div>
</div>
<!--订单管理主体部分 结束-->
<script type="text/javascript">TouchSlide({ slideCell:"#hyzx-order-slide"}); </script>

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
