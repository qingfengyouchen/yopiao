<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
	<title><%=Const.SYSTEM_NAME%>管理后台</title>
    <link href="${ctx }/static/mould/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx }/static/mould/css/font-awesome.min.css" rel="stylesheet">
    <link href="${ctx }/static/mould/css/animate.css" rel="stylesheet">
    <link href="${ctx }/static/mould/css/style.css" rel="stylesheet">
</head>

<body class="fixed-sidebar full-height-layout gray-bg">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div class="dropdown profile-element">
                            <span><img alt="image" class="img-circle" src="${ctx }/static/mould/img/profile_small.jpg" /></span>
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                                <span class="block m-t-xs"><strong class="font-bold"></strong></span>
                                <span class="text-muted text-xs block">${us.name}<b class="caret"></b></span>
                                </span>
                            </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
                                <li><a class="J_menuItem" onclick="changePwd()">修改密码</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="${ctx}/logout">安全退出</a>
                                </li>
                            </ul>
                        </div>
                        <div class="logo-element">H+
                        </div>
                    </li>
                    <shiro:hasAnyPermissions name="goods:goodsCategory:view,goods:goods:view">
	                    <li>
	                        <a href="#"><i class="fa fa-archive"></i><span class="nav-label">商品管理</span><span class="fa arrow"></span></a>
	                        <ul class="nav nav-second-level">
	                            <li>
	                                <a class="J_menuItem" href="${ctx }/goods/goodsCategory/list">商品类别管理</a>
	                            </li>
	                            <li>    
	                                <a class="J_menuItem" href="${ctx }/goods/goods/list">商品管理</a>
	                            </li>
	                        </ul>
	                    </li>
					</shiro:hasAnyPermissions>
					<shiro:hasAnyPermissions name="goods:goodsTimes:view,goods:shareGoods:view,goods:goodsTimes:settingDraw,goods:winner:view">
	                    <li>
	                        <a href="#"><i class="fa fa-gift"></i><span class="nav-label">夺宝管理</span><span class="fa arrow"></span></a>
	                        <ul class="nav nav-second-level">
								<shiro:hasAnyPermissions name="goods:goodsTimes:view">
									<li>
										<a class="J_menuItem" href="${ctx }/goods/goodsTimes/list">商品期号管理</a>
									</li>
								</shiro:hasAnyPermissions>
								<shiro:hasAnyPermissions name="goods:goodsTimes:settingDraw">
									<li>
										<a class="J_menuItem" href="${ctx }/goods/goodsTimes/list-setting">预设中奖</a>
									</li>
								</shiro:hasAnyPermissions>
								<shiro:hasAnyPermissions name="goods:winner:view">
	                            <li>
									<a class="J_menuItem" href="${ctx }/goods/goodsTimes/listWinng">中奖用户管理</a>
	                            </li>
								</shiro:hasAnyPermissions>
								<shiro:hasAnyPermissions name="goods:shareGoods:view">
	                            <li>
									<a class="J_menuItem" href="${ctx }/goods/shareGoods/list">晒单管理</a>
	                            </li>
								</shiro:hasAnyPermissions>
	                        </ul>
	                    </li>
                   	</shiro:hasAnyPermissions>

					<shiro:hasAnyPermissions name="member:member:view,member:memberLevel:view">
	                    <li>
	                        <a href="#"><i class="fa fa-street-view"></i> <span class="nav-label">会员管理</span><span class="fa arrow"></span></a>
	                        <ul class="nav nav-second-level">
	                        	<!--  
	                            <li><a class="J_menuItem" href="${ctx }/member/memberLevel/list">会员等级管理</a>
	                            </li>
	                            -->
	                            <li><a class="J_menuItem" href="${ctx }/member/member/list">会员管理</a>
	                            </li>
	                            <li><a class="J_menuItem" href="${ctx }/member/member/recharge">充值查询</a>
	                            </li>
	                            <li><a class="J_menuItem" href="${ctx }/member/member/redpack">红包查询</a>
	                            </li>
	                        </ul>
	                    </li>
                    </shiro:hasAnyPermissions>

					<shiro:hasAnyPermissions name="notice:notice:view,notice:noticeMessage:view,notice:noticeWithdraw:view,notice:noticeOnlySend:view">
	                    <li>
	                        <a href="#"><i class="fa fa-bell-o"></i> <span class="nav-label">消息管理</span><span class="fa arrow"></span></a>
	                        <ul class="nav nav-second-level">
	                        <shiro:hasAnyPermissions name="notice:notice:view">
	                            <li><a class="J_menuItem" href="${ctx }/notice/notice/list">消息管理</a>
	                            </li>
	                        </shiro:hasAnyPermissions>
	                        <shiro:hasAnyPermissions name="notice:noticeMessage:view,notice:noticeWithdraw:view,notice:noticeOnlySend:view">
	                            <li><a class="J_menuItem" href="${ctx }/notice/noticeMessage/list">会员消息管理</a>
	                            </li>
	                        </shiro:hasAnyPermissions>
	                        </ul>
	                    </li>
                    </shiro:hasAnyPermissions>
					<%-- 
					<shiro:hasAnyPermissions name="service:flowerArtist:view,service:job:view,service:recruitment:view,service:delegateDrive:view,service:activity:view">
						<li>
							<a href="#"><i class="fa fa-folder-open-o"></i> <span class="nav-label">云购管理</span><span class="fa arrow"></span></a>
							<ul class="nav nav-second-level">
								<li>
									<a class="J_menuItem" href="${ctx }/service/flowerArtist/list">鲜花速递 </a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/microBusiness/list">E购微商 </a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/businessInfo/list">E购商业 </a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/convenience/list">便民服务 </a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/funFood/list">休闲美食  </a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/rooftopGossip/list">E购八卦</a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/tutoring/list">私教补习 </a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/job/list">求职  </a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/recruitment/list">招聘工作   </a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/delegateDrive/list">顺风代驾  </a>
								</li> 
								<li>
									<a class="J_menuItem" href="${ctx }/service/activity/list">云购活动   </a>
								</li>
								<li>
									<a class="J_menuItem" href="${ctx }/service/bulletin/list">云购公告   </a>
								</li>
							</ul>
						</li>
					</shiro:hasAnyPermissions>
					--%>

					<shiro:hasAnyPermissions name="sys:imageSetting:view,sys:config:view">
	                    <li>
	                        <a href="#"><i class="fa fa-cog"></i> <span class="nav-label">系统设置</span><span class="fa arrow"></span></a>
	                        <ul class="nav nav-second-level">
	                        <shiro:hasAnyPermissions name="sys:imageSetting:view">
	                            <li><a class="J_menuItem" href="${ctx }/sys/imageSetting/2/list">首页顶部图片设置</a></li>
	                        </shiro:hasAnyPermissions>
	                        <shiro:hasAnyPermissions name="sys:imageSetting:view">
	                            <li><a class="J_menuItem" href="${ctx }/sys/imageSetting/3/list">发现设置</a></li>
	                        </shiro:hasAnyPermissions>
	                            <!--  
	                            <li><a class="J_menuItem" href="${ctx }/sys/config/redpackactivitytime/list">红包活动时间设置</a></li>
	                            -->
	                            <shiro:hasAnyPermissions name="sys:config:view">
	                            <li><a class="J_menuItem" href="${ctx }/sys/config/list">系统设置</a></li>
	                            </shiro:hasAnyPermissions>
	                        </ul>
	                    </li>
                    </shiro:hasAnyPermissions>

					<shiro:hasAnyPermissions name="wx:menu:view">
						<li>
							<a href="${ctx}/wxweb/menu/list" class="J_menuItem">微信菜单设置设置</a>
						</li>
					</shiro:hasAnyPermissions>

					<shiro:hasAnyPermissions name="sys:config:view,sys:user:view,sys:role:view,sys:permission:view,sys:module:view">
	                    <li>
	                        <a href="#"><i class="fa fa-cogs"></i> <span class="nav-label">系统管理</span><span class="fa arrow"></span></a>
	                        <ul class="nav nav-second-level">
	                        	<!--  
	                            <li>
	                            	<a class="J_menuItem" href="${ctx }/sys/category/list">系统类别</a>
	                            </li>
	                            -->
	                            <li>
	                            	<a class="J_menuItem" href="${ctx }/sys/user/list">用户管理</a>
	                            </li>
	                            <li>
	                            	<a class="J_menuItem" href="${ctx }/sys/role/list">角色管理</a>
	                            </li>
	                            <!-- 
	                            <li>
	                            	<a class="J_menuItem" href="${ctx }/sys/permission/list">权限管理</a>
	                            </li>
	                            <li>
	                            	<a class="J_menuItem" href="${ctx }/sys/module/list">模块管理</a>
	                            </li>
	                             -->
	                        </ul>
	                    </li>
                    </shiro:hasAnyPermissions>
                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row content-tabs">
                <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
                </button>
                <nav class="page-tabs J_menuTabs">
                    <div class="page-tabs-content">
                        <a href="javascript:;" class="active J_menuTab">首页</a>
                    </div>
                </nav>
                <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
                </button>
                <div class="btn-group roll-nav roll-right">
                    <button class="dropdown J_tabClose"  data-toggle="dropdown">关闭操作<span class="caret"></span>

                    </button>
                    <ul role="menu" class="dropdown-menu dropdown-menu-right">
                        <li class="J_tabShowActive"><a>定位当前选项卡</a>
                        </li>
                        <li class="divider"></li>
                        <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                        </li>
                        <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                        </li>
                    </ul>
                </div>
                <a href="${ctx}/logout" class="roll-nav roll-right J_tabExit"><i class="fa fa fa-sign-out"></i> 退出</a>
            </div>
            <div class="row J_mainContent" id="content-main">
                <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="${ctx}/dataIndex" frameborder="0" data-id="${ctx}/dataIndex/" seamless></iframe>
            </div>
            <div class="footer">
                <div class="pull-right">Copyright 2016 Weekend All Rights Reserved. 陕西乐久久网络科技有限公司</div>
            </div>
        </div>
        <!--右侧部分结束-->
    </div>

    <!-- 全局js -->
    <script src="${ctx }/static/mould/js/jquery-2.1.1.min.js"></script>
    <script src="${ctx }/static/mould/js/bootstrap.min.js?v=3.4.0"></script>
    <script src="${ctx }/static/mould/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="${ctx }/static/mould/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="${ctx }/static/mould/js/plugins/layer/layer.min.js"></script>
	<script src="${ctx }/static/js/utils/public.js"></script>
    <!-- 自定义js -->
    <script src="${ctx }/static/mould/js/hplus.js?v=3.2.0"></script>
    <script type="text/javascript" src="${ctx }/static/mould/js/contabs.js"></script>
    <!-- layer javascript -->
    <script src="${ctx }/static/mould/js/plugins/layer/layer.min.js"></script>
    
    <script type="text/javascript">
    	$(function(){
    		$("#side-menu li").click(function(){
    			$("body").removeClass("mini-navbar");
    		});
    	});   
    	
    	function changePwd(){
	    	var html = '<div class="ibox-content" style="border-style:none;">'+
	            '<form class="form-horizontal">'+
	                '<div class="form-group">'+
	                    '<label class="col-sm-3 control-label">原密码：</label>'+
	                    '<div class="col-sm-8">'+
	                        '<input type="password" name="oldPassword" id="oldPassword" placeholder="原密码" class="form-control">'+
	                    '</div>'+
	               '</div>'+
	               '<div class="form-group">'+
	                    '<label class="col-sm-3 control-label">新密码：</label>'+
	                    '<div class="col-sm-8">'+
	                        '<input type="password" name="password" id="password" placeholder="新密码" class="form-control">'+
	                    '</div>'+
	                '</div>'+
	            '</form>'+
            '</div>';
    		
	    	layer.alert(html,{
		    		type : 1,
		    	    title: '修改${us.userName}密码',
		    	    btn: ['确定','取消'],
		    	    shift:6,
		    	    closeBtn:2 ,
		    		area: ['400px', 'auto']
            	},function(index, layero){
    		    	var oldPassword = $("#oldPassword").val();
    				var password = $("#password").val();
    				if(Public.isNull(oldPassword)){
    					Public.message("请输入原密码");
    					return;
    				}
    				if(Public.isNull(password)){
    					Public.message("请输入新密码");
    					return;
    				}
    				var len = password.length;
    				if(len < 8 || len > 16){
    					Public.message("新密码的长度为8-16位字符");
    					return;
    				}
    	
    				$.ajax({
    					url: '${ctx}/sys/user/changePassword',
    					data: {'oldPassword': oldPassword,'password': password},
    					type: 'POST',
    					dateType: 'text',
    					success: function(result){
    						if("1" == result){
    							layer.msg("修改密码成功，请重新登录",{
    								icon: 1,
    								time: 2000
    							},function(){
    								window.location.href = '${ctx}/logout';
    							});
    						}else if("2" == result){
    							Public.message("新密码格式不正确，长度为8-16位，且必须包含数字、字母、符号两种以上");
    						}else if("3" == result){
    							Public.message("原密码不正确，不能修改");
    						}else if("4" == result){
    							layer.msg("当前登录失效，请重新登录",{icon: 2});
    						}else{
    							layer.msg("修改密码失败",{icon: 2});
    						}
    					},
    					error: function(){
    						layer.msg("连接服务器失败",{icon: 2});
    					}
    				});
            	});    
		}
    </script>
</body>

</html>

