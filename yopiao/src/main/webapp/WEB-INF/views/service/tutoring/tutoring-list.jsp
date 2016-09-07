<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
<title>私教补习</title>
<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>
<body>
<form:form action="${ctx}/service/tutoring/list" id="frmSearch" cssClass="form-horizontal" modelAttribute="paramsEntity">
	<div class="form-group" style="padding-top: 10px;">
			<label class="col-sm-1 control-label">类别：</label> 
	        <div class="col-sm-2">
				<form:select path="params[type]"  cssClass="form-control">
	           		<form:option value="">全部</form:option>
	                <form:options items="${tutoringMap}"></form:options>
	            </form:select>
	        </div>    
		 	<label class="col-sm-1 control-label">标题名称：</label>
		 	<div class="col-sm-6">
			 	<form:input path="params[title]" id="search" cssClass="form-control"/>
			    <a id="btnSearch" class="btn btn-success" href="javascript:;"><i class="fa fa-search"></i> 查询</a>
				<shiro:hasPermission name="service:flowerArtist:edit">
				    <a id="btnAdd" class="btn btn-success" href="javascript:;"><i class="fa fa-plus"></i> 新增</a>
				    <a id="btnDelete" class="btn btn-success" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
				</shiro:hasPermission>
			</div>
    </div>
	<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
        <thead>
        <tr class="success">
            <th style="text-align: center; width: 20px;"><input type="checkbox"/></th>
            <th style="text-align: center; width: 20%">类别</th>
            <th style="text-align: center; width: 20%">标题</th>
            <th style="text-align: center; width: 20%">联系人</th>
            <th style="text-align: center; width: 20%">联系电话</th>
            <th style="text-align: center; width: 20%">操作</th>
        </tr>
        </thead>
        <tbody id="tbdContent">
        	<jsp:include page="tutoring-list-content.jsp" flush="true"></jsp:include>
		</tbody>
    </table>	
 </form:form>
   <jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
   <script type="text/javascript">
   $(function(){
		$table = $('#contentTable').initTable({
			namespace: '${ctx}/service/tutoring',
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