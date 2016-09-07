<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<c:if test="${not empty message}">
<script type="text/javascript">
/* 	var msg = '<div id="message" class="msgTop alert alert-success">' +
			'<button data-dismiss="alert" class="close" title="关闭">×</button>${message}</div>';
	$('body').children().first().before($(msg));
	setTimeout(function(){
		$("#message").remove();
	}, 5000); */
	toastr.success('${message}', '温馨提示');
</script>
</c:if>