<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>场馆介绍</title>
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
    	<h3>场馆介绍</h3>
        <div class="back"><a href="javascript:history.go(-1)">返回</a></div>
        <div class="more"><a href="#"></a></div>
    </div>
</div>
<div class="hg50"></div>
<!--头部通用 结束-->
<!--场馆介绍主体部分 开始-->
<div class="cg-intro w_100">
   <div class="cgmap w_100"><img src="${rootUri}/static/wx/images/cg_intro_map.jpg" width="100%" /></div>
   <div class="cg-top mb10">
       <h3 class="cg-comtitle">乐事体育生态中心</h3>
       <p>地址：北京市海淀区复兴路88好</p>
   </div>
   <div class="cg-mid mb10">
   	   <h3 class="cg-comtitle">场馆介绍</h3>
       <p>首都体育馆位于白石桥首都体育馆位于白石桥首都体育馆位于白石桥首都体育馆位于白石桥首都体育馆位于白石桥首都体育馆位于白石桥首都体育馆位于白石桥首都体育馆位于白石桥首都体育馆位于白石桥88好首都体育馆位于白石桥88好首都体育馆位于白石桥88好</p>
   </div>
   <div class="cg-foot mb10">
   	   <h3 class="cg-comtitle">场馆图景</h3>
       <p><img src="${rootUri}/static/wx/images/cg_intro_foot_img01.jpg" /></p>
   </div>
</div>
<!--场馆介绍主体部分 结束-->

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
