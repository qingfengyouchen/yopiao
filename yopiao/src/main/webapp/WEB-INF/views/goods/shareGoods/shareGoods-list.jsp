<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
	<title>晒单管理</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>
<body>
<form:form action="${ctx}/goods/shareGoods/list" id="frmSearch" cssClass="form-horizontal" modelAttribute="paramsEntity">
	<div class="form-group" style="padding-top: 10px;">
		<label class="col-sm-1 control-label">晒单主题</label>
		<div class="col-sm-2">
			<form:input path="params[title]" id="search" cssClass="form-control"/>
		</div>
	 	<label class="col-sm-1 control-label">审核结果</label>
	 	<div class="col-sm-6">
	 		<form:select path="params[state]" cssClass="span2 textCss">
	          		<form:option value="">全部</form:option>
	               <form:options items="${shareGoodsMap}"></form:options>
			</form:select>
		    <a id="btnSearch" class="btn btn-success" href="javascript:;"><i class="fa fa-search"></i> 查询</a>
			<shiro:hasPermission name="goods:shareGoods:edit">
			    <a id="btnDelete" class="btn btn-success" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
			</shiro:hasPermission>
		</div>
    </div>
	<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
        <thead>
        <tr class="success">
            <th style="text-align: center; width: 20px;"><input type="checkbox"/></th>
            <th style="text-align: center; width: 10%">晒单主题</th>
            <th style="text-align: center; width: 25%">获奖感言</th>
            <th style="text-align: center; width: 20%">晒单人昵称</th>
            <th style="text-align: center; width: 10%">晒单状态</th>
            <th style="text-align: center; width: 20%">商品期号</th>
            <th style="text-align: center; width: 30%">操作</th>
        </tr>
        </thead>
        <tbody id="tbdContent">
        <jsp:include page="shareGoods-list-content.jsp" flush="true"></jsp:include>
        </tbody>
    </table>	
 </form:form>
   <jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
   <script type="text/javascript">
   $(function(){
		$table = $('#contentTable').initTable({
			namespace: '${ctx}/goods/shareGoods',
			formId:'frmSearch',
			listMethod:'/list-content',
			editMethod: '/edit/{id}',
			deleteMethod: '/delete',
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
