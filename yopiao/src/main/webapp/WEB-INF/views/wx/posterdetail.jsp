<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>海报详情</title>
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
    	<h3>海报详情</h3>
        <div class="back"><a href="javascript:history.go(-1)">返回</a></div>
        <div class="more"><a href="#"></a></div>
    </div>
</div>
<div class="hg50"></div>
<!--头部通用 结束-->
<!--海报详情主体部分 开始-->
<div class="nry-main w_100">
	<div class="nry-top">
    	<!-- <div class="back"><a href="#"><img src="${rootUri}/static/wx/images/nry_icon01.png" /></a></div> -->
        <div class="love red">12312</div>
    	<div class="img"><img src="${rootUri}/static/wx/images/nry_img01.jpg" /></div>
        <div class="jj orangebg">抢购中 &gt;<span>价格段：</span>200-1500元<span>剩余：</span>1000张</div>
    </div>
    <div class="nry-detail mb10">
    	<div class="showdetail mb10"><a href="${rootUri}/wx/url?url=concertdetail"><span>&gt;</span>查看演唱会详情</a></div>
        <dl class="clearfix">
            <dt>地区：</dt>
            <dd><a href="#" class="active">北京</a><a href="#">上海</a><a href="#">北京</a></dd>
        </dl>
        <dl class="clearfix">
        	<dt>时间：</dt>
            <dd><a href="#" class="active">2016-07-08</a><a href="#">2016-07-18</a><a href="#">2016-07-28</a></dd>
        </dl>
        <dl class="clearfix">
        	<dt>价格：</dt>
            <dd><a href="#" class="disable">A区:500</a><a href="#" class="active">B区:600</a><a href="#">C区:800</a></dd>
        </dl>
    </div>
    <div class="nry-tkprovider mb10">
    	<div class="nry-comtitle">供货商</div>
        <ul class="clearfix">
        	<li><div class="img"><a href="${rootUri}/wx/url?url=merchants"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></a></div><div class="right"><h4><a href="#" class="follow"><img src="${rootUri}/static/wx/images/follow_icon01.png" />关注</a><a href="${rootUri}/wx/url?url=merchants" class="name">票商名称</a></h4><p><span>剩余：<i>100张</i></span>价格：<i>￥1500元</i></p></div></li>
            <li><div class="img"><a href="${rootUri}/wx/url?url=merchants"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></a></div><div class="right"><h4><a href="#" class="follow on"><img src="${rootUri}/static/wx/images/follow_icon02.png" />已关注</a><a href="${rootUri}/wx/url?url=merchants" class="name">票商名称</a></h4><p><span>剩余：<i>100张</i></span>价格：<i>￥1500元</i></p></div></li>
            <li><div class="img"><a href="${rootUri}/wx/url?url=merchants"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></a></div><div class="right"><h4><a href="#" class="follow on"><img src="${rootUri}/static/wx/images/follow_icon02.png" />已关注</a><a href="${rootUri}/wx/url?url=merchants" class="name">票商名称</a></h4><p><span>剩余：<i>100张</i></span>价格：<i>￥1500元</i></p></div></li>
        </ul>
    </div>
    <div class="nry-evaluate mb10">
    	<div class="nry-comtitle">用户评论答疑区：</div>
        <div class="nry-evaluate-main">
        	<!--1 开始-->
        	<div class="evaluate-item">
                <div class="ask">
                    <h3>小苹果：</h3>
                    <p class="content">场地是什么类型的，看台在哪里？</p>
                    <p class="time">2016-06-14 09:02</p>
                </div>
                <div class="reply">
                    <p class="content">尊敬的用户您好，我也不知道场地是什么类型，看台在哪里，感谢您对我的支持！</p>
                    <p class="time">2016-06-14 09:02</p>
                </div>
             </div>  
             <!--1 结束--> 
             <!--1 开始-->
        	<div class="evaluate-item">
                <div class="ask">
                    <h3>小苹果：</h3>
                    <p class="content">场地是什么类型的，看台在哪里？</p>
                    <p class="time">2016-06-14 09:02</p>
                </div>
                <div class="reply">
                    <p class="content">尊敬的用户您好，我也不知道场地是什么类型，看台在哪里，感谢您对我的支持！</p>
                    <p class="time">2016-06-14 09:02</p>
                </div>
             </div>  
             <!--1 结束--> 
        </div>
    </div>
    <div class="nry-foot"><a href="${rootUri}/wx/url?url=posterqa">查看更多疑问</a><a href="#" id="user-ask">我要提问</a></div>
    <div class="hg71"></div>
    <div class="comfoot-fixed"><input id="nry-tc-buy" class="combtn btnbg" type="button" value="立即购买" /></div>
    
</div>
<!--海报详情主体部分 结束-->
<!--弹出背景-->
<div class="fadebg" id="fadebg"></div>
<!--立即购买弹出-->
<div class="nry-tc-buydetail" id="nry-tc-buydetail">
    <div class="tc-close" id="tc-close"><img src="${rootUri}/static/wx/images/nry_tc_close.png" /></div>
    <div class="tc-top">
        <div class="img"><img src="${rootUri}/static/wx/images/nry_tc_img01.jpg" /></div>
        <div class="right">
            <h3>张惠妹乌托邦演唱会</h3>
            <p><span class="grey">库存：</span><span class="orange">1000张</span></p>
            <p>演出时间：<span class="orange">2016年6月14日</span></p>
        </div>
    </div>
    <div class="tc-detail mb10">
        <dl class="clearfix">
            <dt>地区：</dt>
            <dd><a href="#" class="active">北京</a><a href="#">上海</a><a href="#">北京</a></dd>
        </dl>
        <dl class="clearfix">
            <dt>价格：</dt>
            <dd><a href="#" class="disable">500</a><a href="#" class="active">600</a><a href="#">800</a></dd>
        </dl>
    </div>
    <div class="tc-num clearfix"><span>数量：</span><a class="down">-</a><input class="numtxt" type="text" value="1" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"><a class="up">+</a><span>合计：<strong class="orange">600元</strong></span></div>
    <input class="combtn btnbg" type="button" value="下一步" />
</div>

<script>
//立即购买弹出
$("#nry-tc-buy").click(function(){
    $("#nry-tc-buydetail,#fadebg").addClass("active");
});
$("#fadebg,#tc-close").click(function(){
    $("#nry-tc-buydetail,#fadebg").removeClass("active");
});
//获得文本框对象
var t = $(".tc-num .numtxt");
$(".tc-num .up").click(function(){
	t.val(parseInt(t.val())+1);
})
$(".tc-num .down").click(function(){
	t.val(parseInt(t.val())-1);
	if(parseInt(t.val())<1){
		t.val(1);
	}
})
</script>
<!--评论弹出-->
<div class="nry-tc-comment" id="user-ask-comment">
	<div class="tc-close" id="tc-close2"><img src="${rootUri}/static/wx/images/nry_tc_close.png" /></div>
	<h3>评论</h3>
    <textarea class="comtare mb10" placeholder="请输入评论内容"></textarea>
    <input class="combtn btnbg" type="button" value="提交" />
</div>
<script>
//立即购买弹出
$("#user-ask").click(function(){
    $("#user-ask-comment,#fadebg").addClass("active");
});
$("#fadebg,#tc-close2").click(function(){
    $("#user-ask-comment,#fadebg").removeClass("active");
});
</script>
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
