<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑晒单信息</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/js/webuploader/webuploader.css"/>
	<link rel="stylesheet" href="${ctx}/static/js/lightbox/visuallightbox.css" type="text/css" />
	<link rel="stylesheet" href="${ctx}/static/js/lightbox/vlightbox.css" type="text/css" />
	
    
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						晒单信息管理 > ${empty entity.id ? "新增" : "修改"}晒单
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/goods/shareGoods/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">晒单主题</label>
							<div class="col-sm-6">
								<form:input path="title" cssClass="form-control" placeholder="内容不少于6个字" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">获奖感言</label>
							<div class="col-sm-6">
								<form:textarea path="content" cssClass="form-control" placeholder="内容不少于30个字" style="height:200px;"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">晒单人昵称</label>
							<div class="col-sm-6">
								<form:input path="userNickName" cssClass="form-control"  autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">晒单状态</label>
							<div class="col-sm-6">
								<form:bsradiobuttons path="state" items="${shareGoodsMap}" labelCssClass="radio-inline"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">缩略图</label>
							<div class="col-sm-6">
								<div id="fileList1" class="uploader-list">
									<c:if test="${not empty entity.thumbnail}">
										<div id="vlightbox">
											<c:forEach items="${detailsImageList}" var="image">
												<div class="file-item thumbnail" imgId="${image.id}">
													<a id="firstImage" href="${shareGoodsRootUri}/${image.url}" class="vlightbox">
													<img src="${shareGoodsRootUri}/${image.url}" /></a>
													<div class="del" onclick="delImg(this, 'thumb')">X</div>
												</div>
											</c:forEach>
										</div>
									</c:if>
								</div>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-2">
								<button class="btn btn-primary" type="submit" id="submit_btn">保存内容</button>
								<button class="btn btn-white" id="btnBack" type="button">返回</button>
							</div>
						</div>
						<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
					</form:form>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
        var $VisualLightBoxParams$ = {autoPlay:true,borderSize:21,enableSlideshow:true,overlayOpacity:0.4,startZoom:true};
    </script>
    <script type="text/javascript" src="${ctx}/static/js/lightbox/visuallightbox.js"></script>
	
	<script>
		$(document).ready(function() {
		    $.validator.setDefaults({
		        highlight: function (element) {
		            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
		        },
		        success: function (element) {
		            element.closest('.form-group').removeClass('has-error').addClass('has-success');
		        },
		        errorElement: "span",
		        errorPlacement: function (error, element) {
		            if (element.is(":radio") || element.is(":checkbox")) {
		                error.appendTo(element.parent().parent().parent());
		            } else {
		                error.appendTo(element.parent());
		            }
		        },
		        errorClass: "help-block m-b-none",
		        validClass: "help-block m-b-none"
		    });
			
			var icon = "<i class='fa fa-times-circle'></i> ";
			$("#inputForm").validate({
				rules: {
					title: {
						rangelength: [6, 64],
						required: true
					}
			        //,
					//content: {
					//	rangelength: [30,200],
					//	required: true
					//}
				},
				messages: {
					name: {
						rangelength: icon +'内容不少于6个字'
					}
				    //,
					//content	: {
					//	rangelength: icon +'内容不少于30个字'
					//}
				},
	        	errorPlacement:function(error,element) {  
	        		error.appendTo(element.parent("div").next("label"));
	           }
			});
			$("#submit_btn").click(function(){
				$("#inputForm").submit();
			});
		});
		
		
		function delImg(_this, type){
			var $parent = $(_this).parent();
			var imgId = $parent.attr('imgId');
			if(imgId){
				if(confirm('确定要删除图片吗？')){
					$.ajax({
						url: '${ctx}/goods/shareGoods/deleteImg',
						data:{
							imgId: imgId,
						},
						type: 'post',
						dataType: 'text',
						success: function(result){
							if(1 == result){
								$parent.remove();
								alert("删除成功");
							}else{
								alert("删除失败");
							}
						},
						error: function(){
							alert("操作失败");
						}
					});
				}
			}
		}

	</script>
</body>
</html>
