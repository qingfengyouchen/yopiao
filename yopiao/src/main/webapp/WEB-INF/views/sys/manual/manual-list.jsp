<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>后台手动操作</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
</head>

<body>

<h2>1. 生成所有商品的详情页面</h2>
<input type="button" class="btn btn-primary"
	   value=" 生成 "  onclick="createAllHtml()"/>

<h2>2. 重发失败的红包</h2>
<input type="button" class="btn btn-primary"
	   value=" 重发 "  onclick="resendFailRedPack()"/>

<jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
<script>
	function createAllHtml(){
		$.ajax({
			url: '${ctx}/sys/manual/createAllHtml',
			type: 'get',
			dataType: 'text',
			success: function(result){
				alert(result == 1 ? "操作成功" : "操作失败");
			},
			error: function(){
				alert("操作失败");
			}
		});
	}

	function resendFailRedPack(){
		$.ajax({
			url: '${ctx}/sys/manual/resendFailRedPack',
			type: 'get',
			dataType: 'text',
			success: function(result){
				alert(result == 1 ? "操作成功" : "操作失败");
			},
			error: function(){
				alert("操作失败");
			}
		})
	}
</script>
</body>
</html>
