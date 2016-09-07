<%@ page import="com.zx.stlife.base.UserUtils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<title>栏目管理</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
<form:form action="${ctx}/sys/channel/list" id="frmSearch" cssClass="form-search" modelAttribute="paramsEntity">
	<form:hidden path="params[category]"/>
	<div class="row condition-bar" >
		<div>
			<label>名称：</label><form:input path="params[name]"/>
			<a id="btnSearch" class="btn" href="javascript:;"><i class="icon-search"></i> 查询</a>
			<shiro:hasAnyPermissions name="jjfw:category:edit,query:query:edit,article:article:edit">
			    <a id="btnAdd" class="btn" href="javascript:;"><i class="icon-plus"></i> 新增</a>
			    <a id="btnDelete" class="btn" href="javascript:;"><i class="icon-remove"></i> 删除</a>
			</shiro:hasAnyPermissions>
	    </div>
	</div>	
			
	<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
		<thead>
			<tr class="success">
				<th style="text-align: center; width: 20px;"><input type="checkbox"/></th>
				<th style="text-align: center; width: 30%;">图片</th>
				<th style="text-align: center; width: 45%;">名称</th>
				<th style="text-align: center; width: 10%;">序号</th>
				<th style="text-align: center; width: 15%;">操作</th>
			</tr>
		</thead>
		<tbody id="tbdContent">
			<jsp:include page="channel-list-content.jsp" flush="true"></jsp:include>
		</tbody>		
	</table>
 </form:form>

<jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
<script type="text/javascript">
   $(function(){
	   $table = $('#contentTable').initTable({
			namespace: '${ctx}/sys/channel',
			formId:'frmSearch',
			listMethod:'/list-content',
			addMethod:'/create?params[category]=${paramsEntity.params.category}',
			editMethod:'/edit/{id}',
			viewMethod: '/view/{id}?params[category]=${paramsEntity.params.category}',
			deleteMethod:'/delete?params[category]=${paramsEntity.params.category}',
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
			container: $('#tbdContent')
		});
   });
   </script>
</body>
</html>
