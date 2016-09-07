<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<title>模块管理</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
<form:form action="${ctx}/sys/area/list" id="frmSearch" cssClass="form-search" modelAttribute="paramsEntity">
	<div class="row condition-bar" >
		<div>
			<tt id="area"></tt>
			 	<label>名称：</label> <form:input path="params[name]"/>
			    <a id="btnSearch" class="btn" href="javascript:;"><i class="icon-search"></i> 查询</a>
			<shiro:hasPermission name="sys:area:edit">
			    <a id="btnAdd" class="btn" href="javascript:;"><i class="icon-plus"></i> 新增</a>
			    <a id="btnDelete" class="btn" href="javascript:;"><i class="icon-remove"></i> 删除</a>
			</shiro:hasPermission>
	    </div>
	</div>	
			
	<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
		<thead>
			<tr class="success">
				<th style="text-align: center; width: 20px;"><input type="checkbox"/></th>
				<th style="text-align: center; width: 60%">名称</th>
				<th style="text-align: center; width: 20%">编码</th>
				<th style="text-align: center; width: 20%">操作</th>
			</tr>
		</thead>
		<tbody id="tbdContent">
			<jsp:include page="area-list-content.jsp" flush="true"></jsp:include>
		</tbody>		
	</table>
 </form:form>
   <jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
	<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
   <script type="text/javascript">
   $(function(){
	   area.init($('#area'), {
		   ctx: '${ctx}',
		   cityName: 'params[cityId]',
		   cityIdVal: '${paramsEntity.params.cityId}',
		   districtName: 'params[districtId]',
		   districtIdVal: '${paramsEntity.params.districtId}',
		   zoneName: 'params[zoneId]',
		   zoneIdVal: '${paramsEntity.params.zoneId}'
	   });
		$table = $('#contentTable').initTable({
			namespace: '${ctx}/sys/area',
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
   </script>
</body>
</html>
