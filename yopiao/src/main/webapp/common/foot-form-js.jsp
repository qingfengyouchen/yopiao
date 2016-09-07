<%@include file="/common/taglibs.jsp" %>
	<input type="hidden" id="listUrl" name="params[listUrl]" />
	<input type="hidden" id="createUrl" name="params[createUrl]"/>
    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
	<script src="${ctx}/static/js/jquery/jquery-1.8.3.min.js"></script>
	<script src="${ctx}/static/js/jqueryui/jquery-ui.min.js"></script>
    <script src="${ctx}/static/js/utils/public.js"></script>
    <script src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script>
    <script src="${ctx}/static/js/jquery-validation/1.14.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/static/js/jquery-validation/1.14.0/jquery.MetaData.js" type="text/javascript"></script>
	<script src="${ctx}/static/js/jquery-validation/1.14.0/jquery.validate.expand.js" type="text/javascript"></script>
	<script src="${ctx}/static/js/jquery-validation/1.14.0/messages_bs_zh.js" type="text/javascript"></script>
	<script src="${ctx}/static/js/biz/area.js" type="text/javascript"></script>
	<!-- Toastr script -->
	<script src="${ctx }/static/mould/js/plugins/toastr/toastr.min.js"></script>

	<!-- layer javascript -->
	<script src="${ctx }/static/mould/js/plugins/layer/layer.min.js"></script>
	<style type="text/css">
		select,
		textarea,
		input[type="text"],
		input[type="password"],
		input[type="datetime"],
		input[type="datetime-local"],
		input[type="date"],
		input[type="month"],
		input[type="time"],
		input[type="week"],
		input[type="number"],
		input[type="email"],
		input[type="url"],
		input[type="search"],
		input[type="tel"],
		input[type="color"],
		.uneditable-input {
		  height: 30px;
		}
	</style>
	<script>
		var form = {
				<c:choose><c:when test="${paramsEntity.params.decode=='false'}">
					listUrl: "${paramsEntity.params.listUrl}",
					createUrl: "${paramsEntity.params.createUrl}"
				</c:when><c:otherwise>
					listUrl: Public.decode("${paramsEntity.params.listUrl}"),
					createUrl: Public.decode("${paramsEntity.params.createUrl}")
				</c:otherwise></c:choose>
		};
		$(function(){
			form.createUrl = Public.appendQuestionMarkOrAnd(form.createUrl,
					'params[listUrl]=${paramsEntity.params.listUrl}&params[createUrl]=${paramsEntity.params.createUrl}'
			);
			$('#listUrl').val(encodeURIComponent(form.listUrl));
			$('#createUrl').val(encodeURIComponent(form.createUrl));
			
			$('#btnBack').click(function(){
				setTimeout(function(){
					location.href = form.listUrl;
				}, 0);
			});

			$("#btnSaveAndAdd").click(function(){
				var flag = true;
				try{
					if(beforeSaveAndAdd){
						flag = beforeSaveAndAdd();
					}
				}catch(e){
				}

				if(flag) {
					var $optType = $("#inputForm #optType");
					if ($optType.length < 1) {
						$optType = $('<input type="hidden" id="optType" name="params[optType]"/>');
						$("#inputForm").append($optType);
					}
					$optType.val("saveAndAdd");
					$("#inputForm").submit();
				}
			});
		});
	</script>