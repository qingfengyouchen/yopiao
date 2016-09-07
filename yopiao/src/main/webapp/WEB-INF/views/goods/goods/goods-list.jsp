<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"> <!--<![endif]-->
<head>
    <title>商品管理</title>
    <jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
<form:form action="${ctx}/goods/goods/list" id="frmSearch" cssClass="form-horizontal" modelAttribute="paramsEntity">
	<div class="form-group" style="padding-top: 10px;">
			<label class="col-sm-1 control-label">商品类别</label>
			<div class="col-sm-2">
				<form:select path="params[goodsCategoryId]" cssClass="form-control">
                    <form:option value="">全部</form:option>
                    <form:options items="${goodsCategoryList}" itemValue="id" itemLabel="name"></form:options>
                </form:select>
			</div>
			<label class="col-sm-1 control-label" style="float: left;">商品状态</label>
			<div class="col-sm-2">
				<form:select path="params[state]" cssClass="form-control">
	           		<form:option value="">全部</form:option>
	                <form:options items="${goodsStateMap}"></form:options>
				</form:select>
			</div>
		 	<label  class="col-sm-1 control-label" style="float: left;">商品名称</label>
		 	<div class="col-sm-5">
			 	<form:input path="params[name]" id="search" cssClass="form-control"/>
			    <a id="btnSearch" class="btn btn-success" href="javascript:;"><i class="fa fa-search"></i> 查询</a>
				<shiro:hasPermission name="goods:goods:edit">
				    <a id="btnAdd" class="btn btn-success" href="javascript:;"><i class="fa fa-plus"></i> 新增</a>
				    <!-- 
				    <a id="btnDelete" class="btn btn-success" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
				     -->
				</shiro:hasPermission>
			</div>
    </div>


    <table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
        <thead>
        <tr class="success">
            <th style="text-align: center; width: 20px;"><input type="checkbox"/></th>
            <th style="text-align: center; width: 15%">图片</th>
            <th style="text-align: center; width: 7%">商品ID</th>
            <th style="text-align: center; width: 20%">商品名称</th>
            <th style="text-align: center; width: 10%">总需要人次</th>
            <th style="text-align: center; width: 15%">类别</th>
            <th style="text-align: center; width: 8%">状态</th>
            <th style="text-align: center; width: 25%">操作</th>
        </tr>
        </thead>
        <tbody id="tbdContent">
        <jsp:include page="goods-list-content.jsp" flush="true"></jsp:include>
        </tbody>
    </table>
</form:form>
<jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
<script type="text/javascript">
    var $table = null;
    $(function () {
        $table = $('#contentTable').initTable({
            namespace: '${ctx}/goods/goods',
            formId: 'frmSearch',
            listMethod: '/list-content',
            addMethod: '/create',
            editMethod: '/edit/{id}',
            viewMethod: '/view/{id}',
            deleteMethod: '/delete',
            btnAddId: 'btnAdd',
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


    function updateState(id, oldState){
        var optType= <%=Const.GoodsState.ENABLE %> == oldState ? "下架" : "上架";
        var title = "确定要" + optType + "吗？";
        Public.confirm(title, function(){
            var index = layer.load();
            $.ajax({
                url: '${ctx}/goods/goods/updateState',
                type: 'post',
                data: {
                    id: id,
                    oldState: oldState
                },
                dataType: 'text',
                success: function (result){
                    Public.message(optType +
                            (result == <%=Const.COMMON_RESULT_SUCCESS%> ? "成功" : "失败") );
                    $table.reloadList();
                    layer.close(index);
                },
                error: function () {
                    Public.message("操作失败");
                    layer.close(index);
                }
            });
        });
    }
</script>
</body>
</html>