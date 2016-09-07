<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"> <!--<![endif]-->
<head>
    <title>中奖信息管理</title>
    <jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
<form:form action="${ctx}/goods/goodsTimes/listWinng" id="frmSearch" cssClass="form-horizontal" modelAttribute="paramsEntity">
	<div class="form-group" style="padding-top: 10px;">
			<label class="col-sm-1 control-label">期号</label>
			<div class="col-sm-2">
				<form:input  path="params[times]" cssClass="form-control"/>
			</div>
			<label class="col-sm control-label" style="float: left;">商品名称</label>
			<div class="col-sm-2">
				<form:input path="params[goodsName]" cssClass="form-control"/>
			</div>
			<!-- 
		 	<label class="col-sm control-label" style="float: left;">状态</label>
		 	<div class="col-sm-4">
		 	 
		 		<form:select path="params[winngState]" id="search" cssClass="form-control">
					<form:option value="">全部</form:option>
					<form:options items="${winngStateMap}"/>
				</form:select>
			</div>
			 -->
			 <label class="col-sm control-label" style="float: left;">状态</label>
		 	<div class="col-sm-4">
		 		<form:select path="params[exchangeState]" id="search" cssClass="form-control">
					<form:option value="">全部</form:option>
					<form:options items="${exchangeStateMap}"/>
				</form:select>
				<a id="btnSearch" class="btn btn-success" href="javascript:;"><i class="fa fa-search"></i> 查询</a>
			</div>
			
			
    </div>
    <table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
        <thead>
        <tr class="success">
            <th style="text-align: center; width: 15%">期号</th>
            <th style="text-align: center; width: 25%">商品名称</th>
            <th style="text-align: center; width: 10%">总需人次</th>
            <th style="text-align: center; width: 20%">中奖用户昵称</th>
            <th style="text-align: center; width: 15%">状态</th>
            <th style="text-align: center; width: 15%">操作</th>
        </tr>
        </thead>
        <tbody id="tbdContent">
        <jsp:include page="goods-times-list-content-winng.jsp" flush="true"></jsp:include>
        </tbody>
    </table>
</form:form>
<jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
<script type="text/javascript">
    var $table = null;
    $(function () {
        $table = $('#contentTable').initTable({
            namespace: '${ctx}/goods/goodsTimes',
            formId: 'frmSearch',
            listMethod: '/list-contentWinng',
            editMethod: '/editWinng/{id}',
            btnDeleteId: 'btnDelete',
            btnSearchId: 'btnSearch',
            formId: 'frmSearch',
            isBindClick: false,//是否绑定行单击事件
            isClickToEdit: true,//单击是否修改，否则是查看
            isCheckAll: true,
            isBindEnter: false,
            checkboxId: 'ids',
            addMethodParams: '',
            deleteMethodParams: '',
            container: $('#tbdContent')//页面容器
        });
    });

</script>
</body>
</html>
