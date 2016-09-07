<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"> <!--<![endif]-->
<head>
    <title>微信公众号菜单管理</title>
    <jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>
<form:form action="${ctx}/wxweb/menu/list" id="frmSearch" cssClass="form-horizontal" modelAttribute="paramsEntity">
    <div class="form-group" style="padding-top: 10px;">
        <div class="col-sm-10">
            <shiro:hasPermission name="wx:menu:edit">
                <a id="btnAdd" class="btn btn-success" href="javascript:;"><i class="fa fa-plus"></i> 新增</a>
                <a id="btnDelete" class="btn btn-success" href="javascript:;"><i class="fa fa-times"></i> 删除</a>
            </shiro:hasPermission>
            <a class="btn btn-success" href="javascript: location.reload(true);"><i class="fa fa-refresh"></i> 重新加载</a>
            <shiro:hasPermission name="wx:menu:sync">
                <a id="btnSyncToWeixin" class="btn btn-success" href="javascript:;"><i class="fa fa-arrow-up"></i>同步到微信公众号</a>
                <a id="btnQueryMenu" class="btn btn-success" href="javascript:;"><i class="fa fa-arrow-down"></i>查看微信自定义菜单</a>
            </shiro:hasPermission>
        </div>
    </div>

    <table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
        <thead>
        <tr class="success">
            <th style="text-align: center; width: 20px;"><input type="checkbox"/></th>
            <th style="text-align: center; width: 20%">名称</th>
            <th style="text-align: center; width: 10%">菜单标识</th>
            <th style="text-align: center; width: 10%">动作类型</th>
            <th style="text-align: center; width: 30%">访问链接</th>
            <th style="text-align: center; width: 10%">消息类型</th>
            <th style="text-align: center; width: 5%">序号</th>
            <th style="text-align: center; width: 15%">操作</th>
        </tr>
        </thead>
        <c:forEach items="${list}" var="entity">
            <tr>
                <td class="fixed text-center"><input type="checkbox" name="ids" value="${entity.id }"/></td>
                <td class="fixed text-left">${entity.name}&nbsp;</td>
                <td class="fixed text-center">${entity.mkey}&nbsp;</td>
                <td class="fixed text-center">${wxMenuTypeMap[entity.type]}&nbsp;</td>
                <td class="fixed text-left" class="">${entity.type == "view" ? entity.url : ""}&nbsp;</td>
                <td class="fixed text-center">${entity.type == "click" ? wxMsgTypeMap[entity.msgType] : ""}&nbsp;</td>
                <td class="fixed text-center">${entity.sortNum}&nbsp;</td>
                <td class="fixed text-center">
                    <shiro:hasPermission name="wx:menu:edit">
                        <a class="btn btn-small aedit" href="javascript:;" id="${entity.id }"><i
                                class="icon-pencil"></i> 修改</a>
                        <a class="btn btn-small adelete" id="${entity.id}" href="javascript:;"><i
                                class="icon-remove"></i> 删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
            <c:forEach items="${entity.childrens}" var="subMenu">
                <tr>
                    <td class="fixed text-center"><input type="checkbox" name="ids" value="${subMenu.id }"/></td>
                    <td class="fixed text-left"><span style="margin-left: 20px;">${subMenu.name}</span>&nbsp;</td>
                    <td class="fixed text-center">${subMenu.mkey}&nbsp;</td>
                    <td class="fixed text-center">${wxMenuTypeMap[subMenu.type]}&nbsp;</td>
                    <td class="fixed text-left" class="">${subMenu.type == "view" ? subMenu.url : ""}&nbsp;</td>
                    <td class="fixed text-center">${subMenu.type == "click" ? wxMsgTypeMap[subMenu.msgType]: ""}&nbsp;</td>
                    <td class="fixed text-center">${subMenu.sortNum}&nbsp;</td>
                    <td class="fixed text-center">
                        <shiro:hasPermission name="wx:menu:edit">
                            <a class="btn btn-small aedit" href="javascript:;" id="${subMenu.id }"><i
                                    class="icon-pencil"></i> 修改</a>
                            <a class="btn btn-small adelete" id="${subMenu.id}" href="javascript:;"><i
                                    class="icon-remove"></i> 删除</a>
                        </shiro:hasPermission>
                    </td>
                </tr>
            </c:forEach>
        </c:forEach>
    </table>
</form:form>
<jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
<jsp:include page="/common/operate-msg.jsp" flush="true"></jsp:include>
<script type="text/javascript">
    $(function () {
        $table = $('#contentTable').initTable({
            namespace: '${ctx}/wxweb/menu',
            formId: 'frmSearch',
            listMethod: '',
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

        <shiro:hasPermission name="wx:menu:sync">
        $("#btnSyncToWeixin").click(function () {
            $.ajax({
                url: "${ctx}/wxweb/menu/sync",
                type: "post",
                datetype: "text",
                success: function (msg) {
                    Public.alert(msg);
                },
                error: function () {
                    Public.alert("同步失败")
                }
            });
        });

        $("#btnQueryMenu").click(function () {
            $.ajax({
                url: "${ctx}/wxweb/menu/queryMenu",
                type: "post",
                datetype: "text",
                success: function (msg) {
                    Public.alert(msg);
                },
                error: function () {
                    Public.alert("查询失败")
                }
            });
        });
        </shiro:hasPermission>
    });
</script>
</body>
</html>
