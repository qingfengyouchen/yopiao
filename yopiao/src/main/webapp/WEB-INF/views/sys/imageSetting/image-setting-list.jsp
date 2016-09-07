<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<title>${categoryName}管理</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
<form:form action="${ctx}/sys/imageSetting/${category}/list" id="frmSearch" cssClass="form-horizontal">
	<div class="row condition-bar" >
		<div>
			<shiro:hasAnyPermissions name="sys:imageSetting:edit">
			    <a id="btnAdd" class="btn btn-success" href="javascript:;"><i class="fa fa-plus"></i> 新增</a>
			    <a id="btnDelete" class="btn btn-success" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
			</shiro:hasAnyPermissions>
	    </div>
	</div>	
			
	<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
		<thead>
			<tr class="success">
				<th style="text-align: center; width: 20px;"><input type="checkbox"/></th>
				<th style="text-align: center; width: 30%;">图片</th>
				<th style="text-align: center; width: 15%;">动作</th>
				<th style="text-align: center; width: 30%;">值</th>
				<th style="text-align: center; width: 10%;">序号</th>
				<th style="text-align: center; width: 15%;">操作</th>
			</tr>
		</thead>
		<tbody id="tbdContent">
			<jsp:include page="image-setting-list-content.jsp" flush="true"></jsp:include>
		</tbody>		
	</table>
 </form:form>

	<jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
<script type="text/javascript">
   $(function(){
	   $table = $('#contentTable').initTable({
			namespace: '${ctx}/sys/imageSetting/${category}',
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
			container: $('#tbdContent')
		});
   });
   </script>
</body>
</html>
