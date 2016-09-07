<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<title>消息管理</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
<form:form action="${ctx}/service/activity/list" id="frmSearch" cssClass="form-horizontal" modelAttribute="paramsEntity">
	<div class="form-group" style="padding-top: 10px;">
		 	<label class="col-sm-2 control-label">标题名称：</label>
		 	<div class="col-sm-10">
			 	<form:input path="params[title]" id="search" cssClass="form-control"/>
			 	<form:hidden path="params[type]" value="1"/>
			    <a id="btnSearch" class="btn btn-success" href="javascript:;"><i class="fa fa-search"></i> 查询</a>
				<shiro:hasPermission name="service:activity:edit">
				    <a id="btnAdd" class="btn btn-success" href="javascript:;"><i class="fa fa-plus"></i> 新增</a>
				    <a id="btnDelete" class="btn btn-success" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
				</shiro:hasPermission>
			</div>
    </div>
			
	<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
		<thead>
			<tr class="success">
				<th style="text-align: center; width: 10px;"><input type="checkbox"/></th>
				<th style="text-align: center; width: 35%">图片</th>
				<th style="text-align: center; width: 35%">标题</th>
				<!-- 
				<th style="text-align: center; width: 10%">活动时间</th>
				<th style="text-align: center; width: 10%">设定人数</th>
				<th style="text-align: center; width: 10%">已报名人数</th>
				<th style="text-align: center; width: 13%">活动地址</th>
				<th style="text-align: center; width: 7%">活动状态</th>
				-->
				<th style="text-align: center; width: 30%">操作</th>
			</tr>
		</thead>
		<tbody id="tbdContent">
			<jsp:include page="activity-list-content.jsp" flush="true"></jsp:include>
		</tbody>		
	</table>
 </form:form>
   <jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
   <jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
   <script type="text/javascript">
   var $table = null;
   $(function(){
		$table = $('#contentTable').initTable({
			namespace: '${ctx}/service/activity',
			formId:'frmSearch',
			listMethod:'/list-content',
			addMethod:'/create',
			editMethod:'/edit/{id}',
			viewMethod: '/view/{id}',
			deleteMethod:'/delete',
			btnAddId: 'btnAdd',
			btnDeleteId: 'btnDelete',
			btnSearchId: 'btnSearch',
			formId:'frmSearch',
			isBindClick: false,//是否绑定行单击事件
			isClickToEdit: true,//单击是否修改，否则是查看
			isCheckAll: true,
			isBindEnter: false,
			checkboxId: 'ids',
			addMethodParams:'',
			deleteMethodParams:'',
			container: $('#tbdContent')//页面容器
		});
   });
   
   
    function updateState(id){
    	Public.confirm("确定要结束活动", function(){
	    	$.post("${ctx}/service/activity/update",{id:id},function(state){
                Public.message("活动结束" +(state == <%=Const.COMMON_RESULT_SUCCESS%> ? "成功" : "失败") );
                $table.reloadList();
	    	});
		 });
    }
   </script>
</body>
</html>
