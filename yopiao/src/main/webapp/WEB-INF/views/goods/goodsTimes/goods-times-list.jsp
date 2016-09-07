<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"> <!--<![endif]-->
<head>
    <title>商品期号管理</title>
    <jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
    <jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
    <script type="text/javascript" src="${ctx}/static/js/utils/moment-with-locales.js"></script>
</head>

<body>
<form:form action="${ctx}/goods/goodsTimes/list" id="frmSearch" cssClass="form-horizontal" modelAttribute="paramsEntity">
	<div class="form-group" style="padding-top: 10px;">
        <label class="col-sm-1 control-label">商品类别</label>
        <div class="col-sm-2">
            <form:select path="params[goodsCategoryId]"  cssClass="form-control">
                <form:option value="">全部</form:option>
                <form:options items="${goodsCategoryList}" itemValue="id" itemLabel="name"></form:options>
            </form:select>
        </div>

        <label class="col-sm-1 control-label">状态</label>
        <div class="col-sm-2">
            <form:select path="params[state]"  cssClass="form-control">
                <form:option value="">全部</form:option>
                <form:options items="${goodsTimesStateMap}" />
            </form:select>
        </div>

		 	<label class="col-sm-1 control-label">商品名称</label>
		 	<div class="col-sm-4">
			 	<form:input path="params[goodsName]" id="search" cssClass="form-control"/>
			    <a id="btnSearch" class="btn btn-success" href="javascript:;"><i class="fa fa-search"></i> 查询</a>
			</div>
    </div>

    <table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
        <thead>
        <tr class="success">
            <th style="text-align: center; width: 10%">图片</th>
            <th style="text-align: center; width: 20%">商品名称</th>
            <th style="text-align: center; width: 10%">期号</th>
            <th style="text-align: center; width: 15%">进度</th>
            <th style="text-align: center; width: 15%">已参与人次/总需人次</th>
            <th style="text-align: center; width: 8%">状态</th>
            <th style="text-align: center; width: 12%">揭晓时间/倒计时</th>
            <th style="text-align: center; width: 10%">操作</th>
        </tr>
        </thead>
        <tbody id="tbdContent">
        <jsp:include page="goods-times-list-content.jsp" flush="true"></jsp:include>
        </tbody>
    </table>
</form:form>
<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
<script type="text/javascript">
    var $table = null;
    $(function () {
        $table = $('#contentTable').initTable({
            namespace: '${ctx}/goods/goodsTimes',
            formId: 'frmSearch',
            listMethod: '/list-content',
            addMethod: '/create',
            editMethod: '',
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

    function produceLuckNum(goodsTimesId){
        if(Public.confirm('确定要开奖吗？', function(){
                    var index;
                    $.ajax({
                        url: '${ctx}/goods/goodsTimes/produceLuckNum',
                        data:{
                            goodsTimesId: goodsTimesId,
                        },
                        type: 'post',
                        dataType: 'text',
                        beforeSend: function(){
                            index = layer.load();
                        },
                        success: function(result){
                            layer.close(index);
                            if(result == "1"){
                                Public.alert("操作成功");
                                $table.reloadList();
                            }else if(result == "2"){
                                Public.alert("操作失败");
                            }
                        },
                        error: function(){
                            layer.close(index);
                            Public.alert("操作失败");
                        }
                    });
                }));

    }
</script>
</body>
</html>
