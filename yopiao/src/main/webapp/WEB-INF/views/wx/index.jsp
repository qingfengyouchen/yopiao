<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>首页</title>
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
    	<h3>约吗</h3>
        <div class="back"><a href="javascript:history.go(-1)">返回</a></div>
        <div class="more"><a href="#"></a></div>
    </div>
</div>
<div class="hg50"></div>
<!--头部通用 结束-->
<!--首页主体部分 开始-->
<div class="index-main w_100">
	<div class="index-top w_100">
        <div class="index-top-search"><a href="${rootUri}/wx/url?url=ticketbus" title="搜索"></a></div>
        <div class="index-top-sort">
            <ul class="clearfix">
                <li class="active"><a href="#">前排票</a></li>
                <li><a href="#">原价票</a></li>
                <li><a href="#">预售票</a></li>
                <li><a href="#">小道消息</a></li>
            </ul>
        </div>
    </div>    
    <!--box 开始-->
    <div class="hg50"></div>
    <div class="index-mian-box w_100">
    	<!--前排票内容 开始-->
    	<div class="index-box-item active">
        	<div class="bannerslide" id="bannerslide">
            	<div class="bd">
            		<ul>
                    	<li><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_banner01.jpg" /></a></li>
                        <li><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_banner01.jpg" /></a></li>
                        <li><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_banner01.jpg" /></a></li>
                    </ul>
                </div>
                <div class="hd">
					<ul></ul>
				</div>
            </div>
            <script type="text/javascript">
				TouchSlide({ 
					slideCell:"#bannerslide",
					titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
					mainCell:".bd ul", 
					effect:"left", 
					autoPlay:true,//自动播放
					autoPage:true, //自动分页
				});
			</script>

        	<div class="index-yplist">
            	<ul class="clearfix">
                	<li><div class="love">1231</div><div class="img"><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_list_img02.jpg" /></a></div><div class="jj">正在开售 &gt;</div></li>
                </ul>
            </div>
        </div>
        <!--前排票内容 结束-->
        <!--原价票内容 开始-->
    	<div class="index-box-item">
        	<div class="bannerslide" id="bannerslide">
            	<div class="bd">
            		<ul>
                    	<li><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_banner01.jpg" /></a></li>
                        <li><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_banner01.jpg" /></a></li>
                        <li><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_banner01.jpg" /></a></li>
                    </ul>
                </div>
                <div class="hd">
					<ul></ul>
				</div>
            </div>
            <script type="text/javascript">
				TouchSlide({ 
					slideCell:"#bannerslide",
					titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
					mainCell:".bd ul", 
					effect:"left", 
					autoPlay:true,//自动播放
					autoPage:true, //自动分页
				});
			</script>

        	<div class="index-yplist">
            	<ul class="clearfix">
                	<li><div class="love">1231</div><div class="img"><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_list_img02.jpg" /></a></div><div class="jj">正在开售 &gt;</div></li>
                </ul>
            </div>
        </div>
        <!--原价票内容 结束-->
        <!--预售票内容 开始-->
    	<div class="index-box-item">
        	<div class="bannerslide2" id="bannerslide2">
            	<div class="bd2">
            		<ul>
                    	<li><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_banner01.jpg" /></a></li>
                        <li><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_banner01.jpg" /></a></li>
                        <li><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_banner01.jpg" /></a></li>
                    </ul>
                </div>
                <div class="hd2">
					<ul></ul>
				</div>
            </div>
            <script type="text/javascript">
			function TouchSlide2(){
					TouchSlide({ 
						slideCell:"#bannerslide2",
						titCell:".hd2 ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
						mainCell:".bd2 ul", 
						effect:"left", 
						autoPlay:true,//自动播放
						autoPage:true, //自动分页
					});
			}
			</script>

        	<div class="index-yplist">
            	<ul class="clearfix">
                	<li><div class="love">1231</div><div class="img"><a href="${rootUri}/wx/url?url=posterdetail"><img src="${rootUri}/static/wx/images/index_list_img02.jpg" /></a></div><div class="jj">剩余12:00:00 现正接受预约 &gt;</div></li>
                </ul>
            </div>
        </div>
        <!--预售票内容 结束-->
        <!--小道消息内容 开始-->
    	<div class="index-box-item">
        	<div class="message-title"><a href="${rootUri}/wx/url?url=gossipdetail" title="更多"></a>官方小道消息<img src="${rootUri}/static/wx/images/message_icon01.png" /></div>
        	<div class="message-list">
            	<ul class="clearfix">
                	<li><a href="${rootUri}/wx/url?url=gossipdetail"><img src="${rootUri}/static/wx/images/message_list_img01.png" /><h3>2016周华健今天唱什么</h3></a></li>
                    <li><a href="${rootUri}/wx/url?url=gossipdetail"><img src="${rootUri}/static/wx/images/message_list_img01.png" /><h3>2016周华健今天唱什么</h3></a></li>
                    <li><a href="${rootUri}/wx/url?url=gossipdetail"><img src="${rootUri}/static/wx/images/message_list_img01.png" /><h3>2016周华健今天唱什么</h3></a></li>
                </ul>
            </div>
        </div>
        <!--小道消息内容 结束-->
	</div>
    <!--box 结束-->
</div>
<script>
var iTrue = true;
$(".index-top-sort ul li").click(function(){
	$(this).addClass("active").siblings().removeClass("active");
	$(".index-mian-box .index-box-item").eq($(this).index()).addClass("active").siblings().removeClass("active");
	if($(".index-box-item").eq(2).hasClass("active") && iTrue){
		TouchSlide2();
		iTrue = false;
	}
});
</script>

<!--首页主体部分 结束-->
<div class="hg50"></div>
<nav class="index-nav">
	<ul class="clearfix">
    	<li class="active"><a href="#"><i><img src="${rootUri}/static/wx/images/index_nav_icon01.png" /></i><p>约吗</p></a></li>
        <li><a href="${rootUri}/wx/url?url=authentication"><i><img src="${rootUri}/static/wx/images/index_nav_icon02.png" /></i><p>求约</p></a></li>
        <li><a href="${rootUri}/wx/url?url=yueba"><i><img src="${rootUri}/static/wx/images/index_nav_icon03.png" /></i><p>约吧</p></a></li>
    </ul>
</nav>
<div class="gotop" id="gotop"><a href="#"><img src="${rootUri}/static/wx/images/gotop_icon.png" /></a></div>
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
