<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<c:if test="${not empty fangpan}">
	<input type="hidden" name="params[fangpanId]" value="${fangpan.id}">
	<div class="control-group">
		<label class="control-label">房屋类型:</label>
		<div class="controls">
				${houseTypeMap[fangpan.houseType]}
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">联系人:</label>
		<div class="controls">
				${fangpan.linker}
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">手机号码:</label>
		<div class="controls">
				${fangpan.mobileNo}
		</div>
	</div>
	<hr>
</c:if>