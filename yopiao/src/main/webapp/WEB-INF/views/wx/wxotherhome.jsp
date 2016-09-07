<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<!--INC.META-->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
<title>约吧</title>
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
    	<h3>约吧</h3>
        <div class="back"><a href="javascript:history.go(-1)">返回</a></div>
        <div class="more"><a href="#"></a></div>
    </div>
</div>
<div class="hg50"></div>
<!--头部通用 结束-->
<!--约吧主体部分 开始-->
<div class="date w_100">
	<div class="date-top">
    	<div class="hyzx-top-info"><img src="${rootUri}/static/wx/images/ych_detail_place_img01.jpg" />小苹果<a href="#" class="follow">+关注</a></div>
        <div class="date-top-img"><img src="${rootUri}/static/wx/images/other_date_banner.jpg" /></div>
    </div>
    <div class="other-date-nav">
    	<ul class="clearfix">
        	<li class="active"><h3>75</h3><p>她的动态</p></li>
            <li><h3>75</h3><p>她的关注</p></li>
            <li><h3>75</h3><p>她的粉丝</p></li>
        </ul>
    </div>
    <div class="data-main">
    	<!--最新-->
    	<div class="data-main-item active">
        	<!--循环1-->
        	<div class="date-comlist mb10">
            	
                <!--图片展示-->
                <div class="date-comlist-pic" id="date-comlist-pic">
                        <!---title-->
                    <div class="date-comlist-title">
                        <div class="l">
                            <img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" />
                        </div>
                        <div class="r">
                            <a href="#" class="my-reply"><img src="${rootUri}/static/wx/images/date_icon01.png" />10</a>
                            <a href="#"><img src="${rootUri}/static/wx/images/date_icon02.png" />10</a>
                        </div>
                    </div>
                	<div class="date-comlist-pic-main bd">
                    	<ul class="clearfix">
                        	<li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                            <li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                            <li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                        </ul>
                    </div>
                    <div class="date-comlist-pic-btn hd">
                        <span class="prev"><img src="${rootUri}/static/wx/images/date_pic_lbtn.png" /></span>
                        <span class="next"><img src="${rootUri}/static/wx/images/date_pic_rbtn.png" /></span>
					</div>

                </div>
                <script type="text/javascript">
					TouchSlide({ slideCell:"#date-comlist-pic", mainCell:".bd ul", effect:"leftLoop" });
				</script>
                <!--底部回复-->
                <div class="date-comlist-reply">
                	<!--回复1 开始-->
                	<div class="user-reply clearfix">
                    	<div class="user-reply-l"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></div>
                        <div class="user-reply-r">
                        	<div class="licon"></div>
                        	<p>小评估：希望所有背负梦想的人都能坚持到底，加油，亲爱的朋友们！相信自己我的未来不是梦！<span class="greyer">2小时前</span></p>
                        </div>
                    </div>
                    <!--回复1 结束-->
                    <!--回复1 开始-->
                	<div class="user-reply clearfix">
                    	<div class="user-reply-l"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></div>
                        <div class="user-reply-r">
                        	<div class="licon"></div>
                        	<p>小评估：希望所有背负梦想的人都能坚持到底，加油，亲爱的朋友们！相信自己我的未来不是梦！<span class="greyer">2小时前</span></p>
                        </div>
                    </div>
                    <!--回复1 结束-->
                </div>
                <div class="user-reply-seeall"><a href="#">查看全部12条评论</a></div>
            </div>
            <!--循环1 结束-->
            <!--循环1-->
        	<div class="date-comlist mb10">
            	
                <!--图片展示-->
                <div class="date-comlist-pic" id="date-comlist-pic">
                        <!---title-->
                    <div class="date-comlist-title">
                        <div class="l">
                            <img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" />
                        </div>
                        <div class="r">
                            <a href="#" class="my-reply"><img src="${rootUri}/static/wx/images/date_icon01.png" />10</a>
                            <a href="#"><img src="${rootUri}/static/wx/images/date_icon02.png" />10</a>
                        </div>
                    </div>
                	<div class="date-comlist-pic-main bd">
                    	<ul class="clearfix">
                        	<li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                            <li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                            <li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                        </ul>
                    </div>
                    <div class="date-comlist-pic-btn hd">
                        <span class="prev"><img src="${rootUri}/static/wx/images/date_pic_lbtn.png" /></span>
                        <span class="next"><img src="${rootUri}/static/wx/images/date_pic_rbtn.png" /></span>
					</div>

                </div>
                <script type="text/javascript">
					TouchSlide({ slideCell:"#date-comlist-pic", mainCell:".bd ul", effect:"leftLoop" });
				</script>
                <!--底部回复-->
                <div class="date-comlist-reply">
                	<!--回复1 开始-->
                	<div class="user-reply clearfix">
                    	<div class="user-reply-l"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></div>
                        <div class="user-reply-r">
                        	<div class="licon"></div>
                        	<p>小评估：希望所有背负梦想的人都能坚持到底，加油，亲爱的朋友们！相信自己我的未来不是梦！<span class="greyer">2小时前</span></p>
                        </div>
                    </div>
                    <!--回复1 结束-->
                    <!--回复1 开始-->
                	<div class="user-reply clearfix">
                    	<div class="user-reply-l"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></div>
                        <div class="user-reply-r">
                        	<div class="licon"></div>
                        	<p>小评估：希望所有背负梦想的人都能坚持到底，加油，亲爱的朋友们！相信自己我的未来不是梦！<span class="greyer">2小时前</span></p>
                        </div>
                    </div>
                    <!--回复1 结束-->
                </div>
                <div class="user-reply-seeall"><a href="#">查看全部12条评论</a></div>
            </div>
            <!--循环1 结束-->
            
        </div>
        <!--最热-->
        <div class="data-main-item">
        	
            <!--循环1-->
        	<div class="date-comlist mb10">
            	
                <!--图片展示-->
                <div class="date-comlist-pic" id="date-comlist-pic">
                        <!---title-->
                    <div class="date-comlist-title">
                        <div class="l">
                            <img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" />
                        </div>
                        <div class="r">
                            <a href="#" class="my-reply"><img src="${rootUri}/static/wx/images/date_icon01.png" />10</a>
                            <a href="#"><img src="${rootUri}/static/wx/images/date_icon02.png" />10</a>
                        </div>
                    </div>
                	<div class="date-comlist-pic-main bd">
                    	<ul class="clearfix">
                        	<li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                            <li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                            <li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                        </ul>
                    </div>
                    <div class="date-comlist-pic-btn hd">
                        <span class="prev"><img src="${rootUri}/static/wx/images/date_pic_lbtn.png" /></span>
                        <span class="next"><img src="${rootUri}/static/wx/images/date_pic_rbtn.png" /></span>
					</div>

                </div>
                <script type="text/javascript">
					TouchSlide({ slideCell:"#date-comlist-pic", mainCell:".bd ul", effect:"leftLoop" });
				</script>
                <!--底部回复-->
                <div class="date-comlist-reply">
                	<!--回复1 开始-->
                	<div class="user-reply clearfix">
                    	<div class="user-reply-l"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></div>
                        <div class="user-reply-r">
                        	<div class="licon"></div>
                        	<p>小评估：最热最热持到底最热最热持到底最热最热持到底最热最热持到底，加油，亲爱的朋友们！相信自己我的未来不是梦！<span class="greyer">2小时前</span></p>
                        </div>
                    </div>
                    <!--回复1 结束-->
                    <!--回复1 开始-->
                	<div class="user-reply clearfix">
                    	<div class="user-reply-l"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></div>
                        <div class="user-reply-r">
                        	<div class="licon"></div>
                        	<p>小评估：希望所有背负梦想的人都能坚持到底，加油，亲爱的朋友们！相信自己我的未来不是梦！<span class="greyer">2小时前</span></p>
                        </div>
                    </div>
                    <!--回复1 结束-->
                </div>
                <div class="user-reply-seeall"><a href="#">查看全部12条评论</a></div>
            </div>
            <!--循环1 结束-->
            
        </div>
        <!--关注-->
        <div class="data-main-item">
        
        	<!--循环1-->
        	<div class="date-comlist mb10">
            	
                <!--图片展示-->
                <div class="date-comlist-pic" id="date-comlist-pic">
                        <!---title-->
                    <div class="date-comlist-title">
                        <div class="l">
                            <img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" />
                        </div>
                        <div class="r">
                            <a href="#" class="my-reply"><img src="${rootUri}/static/wx/images/date_icon01.png" />10</a>
                            <a href="#"><img src="${rootUri}/static/wx/images/date_icon02.png" />10</a>
                        </div>
                    </div>
                	<div class="date-comlist-pic-main bd">
                    	<ul class="clearfix">
                        	<li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                            <li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                            <li><img src="${rootUri}/static/wx/images/date_img01.jpg" /></li>
                        </ul>
                    </div>
                    <div class="date-comlist-pic-btn hd">
                        <span class="prev"><img src="${rootUri}/static/wx/images/date_pic_lbtn.png" /></span>
                        <span class="next"><img src="${rootUri}/static/wx/images/date_pic_rbtn.png" /></span>
					</div>

                </div>
                <script type="text/javascript">
					TouchSlide({ slideCell:"#date-comlist-pic", mainCell:".bd ul", effect:"leftLoop" });
				</script>
                <!--底部回复-->
                <div class="date-comlist-reply">
                	<!--回复1 开始-->
                	<div class="user-reply clearfix">
                    	<div class="user-reply-l"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></div>
                        <div class="user-reply-r">
                        	<div class="licon"></div>
                        	<p>小评估：关注关注关注关注关注关注关注关注，加油，亲爱的朋友们！相信自己我的未来不是梦！<span class="greyer">2小时前</span></p>
                        </div>
                    </div>
                    <!--回复1 结束-->
                    <!--回复1 开始-->
                	<div class="user-reply clearfix">
                    	<div class="user-reply-l"><img src="${rootUri}/static/wx/images/nry_tkprovider_img01.jpg" /></div>
                        <div class="user-reply-r">
                        	<div class="licon"></div>
                        	<p>小评估：希望所有背负梦想的人都能坚持到底，加油，亲爱的朋友们！相信自己我的未来不是梦！<span class="greyer">2小时前</span></p>
                        </div>
                    </div>
                    <!--回复1 结束-->
                </div>
                <div class="user-reply-seeall"><a href="#">查看全部12条评论</a></div>
            </div>
            <!--循环1 结束-->
        
        </div>
    </div>
</div>

<script>
$(".other-date-nav ul li").click(function(){
	$(this).addClass("active").siblings().removeClass("active");
	$(".data-main .data-main-item").eq($(this).index()).addClass("active").siblings().removeClass("active");
});
</script>
<!--评论弹出-->
<div class="nry-tc-comment" id="user-ask-comment" style="padding-top:35px;">
	<div class="tc-close" id="tc-close2"><img src="${rootUri}/static/wx/images/nry_tc_close.png" /></div>
    <textarea class="comtare2 mb10" placeholder="请输入评论内容"></textarea>
    <div class="date-addimg mb5">
    	<dl class="clearfix">
            <dd><img src="${rootUri}/static/wx/images/nry_tc_img01.jpg" /><div class="close"><img src="${rootUri}/static/wx/images/qy_closeicon.png" /></div></dd>
            <dd><img src="${rootUri}/static/wx/images/nry_tc_img01.jpg" /><div class="close"><img src="${rootUri}/static/wx/images/qy_closeicon.png" /></div></dd>
            <dd><img src="${rootUri}/static/wx/images/nry_tc_img01.jpg" /><div class="close"><img src="${rootUri}/static/wx/images/qy_closeicon.png" /></div></dd>
            <dd class="add"><input type="file" class="comfile" /></dd>
        </dl>
    </div>
    <input class="combtn btnbg" type="button" value="发送" />
</div>
<!--弹出背景-->
<div class="fadebg" id="fadebg"></div>
<script>
//评论弹出
$(".my-reply").click(function(){
    $("#user-ask-comment,#fadebg").addClass("active");
});
$("#fadebg,#tc-close2").click(function(){
    $("#user-ask-comment,#fadebg").removeClass("active");
});
</script>

<!--约吧主体部分 结束-->
<div class="hg71"></div>
<div class="hyzx-nav">
	<ul class="clearfix">
    	<li class="active"><a href="#"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon01.png" /></i><p>首页</p></a></li>
        <li><a href="#"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon02.png" /></i><p>好友</p></a></li>
        <li><a href="${rootUri}/wx/url?url=publishmsg"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon03.png" /></i><p>发布</p></a></li>
        <li><a href="#"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon04.png" /></i><p>提醒</p></a></li>
        <li><a href="#"><i><img src="${rootUri}/static/wx/images/hyzx_nav_icon05.png" /></i><p>我</p></a></li>
    </ul>
</div>

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
