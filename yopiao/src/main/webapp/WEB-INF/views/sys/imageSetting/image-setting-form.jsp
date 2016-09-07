<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑${categoryName}</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/js/webuploader/webuploader.css">
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						${categoryName}管理 > ${empty entity.id ? "新增" : "修改"}${categoryName}
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/sys/imageSetting/save"  method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<form:hidden path="category"/>
						<c:set var="indexTopSwitch"><%=Const.ImageCategory.INDEX_TOP_SWITCH%></c:set>
						<c:if test="${indexTopSwitch == entity.category}">
							<div class="form-group">
								<label class="col-sm-2 control-label">动作类型</label>
								<div class="col-sm-6">
									<form:select path="actionType" cssClass="form-control">
										<form:option value="">请选择</form:option>
										<form:options items="${imageActionTypeMap}"></form:options>
									</form:select>
								</div>
								<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">值</label>
								<div class="col-sm-6">
									<form:input path="value" cssClass="form-control" autocomplete="off"/>
								</div>
								<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
							</div>
						</c:if>
						
						<c:set var="indexFindSwitch"><%=Const.ImageCategory.INDEX_FIND_SWITCH%></c:set>
						<c:if test="${indexFindSwitch == entity.category}">
							<div class="form-group">
								<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label">图片的链接地址</label>
								<div class="col-sm-6">
									<form:input path="value" cssClass="form-control" autocomplete="off"/>
								</div>
								<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
							</div>
						</c:if>
						
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">序号</label>
							<div class="col-sm-6">
								<form:input path="sortNum" cssClass="form-control" placeholder="请输入1-99内的整数 " autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">图片:750*360像素</label>
							<div class="col-sm-6">
								<div id="fileList2" class="uploader-list">
									<c:if test="${not empty entity.url}">
										<div class="file-item thumbnail" imgId="${entity.id}">
											<img src="${topSwitchImgRootUri}/${entity.url}" style="width:135px;">
											<div class="del" onclick="delImg(this, 'thumb')">X</div>
										</div>
									</c:if>
								</div>
								<div id="filePicker2" style="display: ${empty entity.url ? "" : "none"};">
									<button class="btn btn-success " type="button"><i class="fa fa-upload"></i>&nbsp;&nbsp;<span class="bold">上传图片</span></button>								
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
	
	<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
	<script type="text/javascript" src="${ctx}/static/js/webuploader/webuploader.min.js"></script>
	<script src="${ctx}/static/js/My97DatePicker/WdatePicker.js"></script>
	<script>
		// Web Uploader实例
		var uploader2;
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
					name: {
						required: true,
						rangelength: [2, 8]
					},
					sortNum: {
						isNumber: true,
						numRange: [1, 99]
					},
					value:{
						required: true
					}
				},
				messages: {
					name: {
						rangelength: icon+'请输入2-8个字符'
					},
					sortNum: {
						isNumber: icon+ '请输入1-99内的整数',
						numRange: icon+'请输入1-99内的整数'
					},
					value:{
						required:icon+'不能为空'
					}
				},
            	errorPlacement:function(error,element) {  
            		error.appendTo(element.parent("div").next("label"));
               }
			});

			var $list2 = $('#fileList2'),
					// 优化retina, 在retina下这个值是2
					ratio = window.devicePixelRatio || 1;

			// 初始化Web Uploader
			uploader2 = WebUploader.create({

				// 自动上传。
				auto: true,

				// swf文件路径
				swf: '${ctx}/static/js/webuploader/Uploader.swf',

				// 文件接收服务端。
				server: '${ctx}/tmpFile/upload',

				// 选择文件的按钮。可选。
				// 内部根据当前运行是创建，可能是input元素，也可能是flash.
				pick: '#filePicker2',

				fileNumLimit: 1, //验证文件总数量, 超出则不允许加入队列。

				// 只允许选择文件，可选。
				accept: {
					title: 'Images',
					extensions: 'jpg,jpeg,bmp,png',
					mimeTypes: 'image/*'
				},
				compress: {
					width: 750,
					height: 360,
					// 图片质量，只有type为`image/jpeg`的时候才有效。
					quality: 100,
					// 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
					allowMagnify: false,
					// 是否允许裁剪。
					crop: true,
					// 是否保留头部meta信息。
					preserveHeaders: true,
					// 如果发现压缩后文件大小比原来还大，则使用原来图片
					// 此属性可能会影响图片自动纠正功能
					noCompressIfLarger: false,
					// 单位字节，如果图片大小小于此值，不会采用压缩。
					compressSize: 0
				}
			});

			// 当有文件添加进来的时候
			uploader2.on( 'fileQueued', function( file ) {
				var $li = $(
								'<div id="' + file.id + '" class="file-item thumbnail">' +
								'<img>' +
									//'<div class="info">' + file.name + '</div>' +
								'<div class="del" onclick="delImg(this, \'thumb\')">X</div>' +
								'</div>'
						),
						$img = $li.find('img');

				$list2.append( $li );

				// 创建缩略图
				uploader2.makeThumb( file, function( error, src ) {
					if ( error ) {
						$img.replaceWith('<span>不能预览</span>');
						return;
					}

					$img.attr( 'src', src );
				}, 150, 72 );
			});

			// 文件上传过程中创建进度条实时显示。
			uploader2.on( 'uploadProgress', function( file, percentage ) {
				var $li = $( '#'+file.id ),
						$percent = $li.find('.progress span');

				// 避免重复创建
				if ( !$percent.length ) {
					$percent = $('<p class="progress"><span></span></p>')
							.appendTo( $li )
							.find('span');
				}

				$percent.css( 'width', percentage * 100 + '%' );
			});

			// 文件上传成功，给item添加成功class, 用样式标记上传成功。
			uploader2.on( 'uploadSuccess', function( file, response) {
				var $container = $( '#'+file.id );
				$container.addClass('upload-state-done');

				var id = typeof(response) == 'object' ? response._raw : response;
				$container.attr("tmpFileId", id);
				var $hiddenFile = $('input[name=tmpFileId]');
				if($hiddenFile.length > 0){
					$hiddenFile.val(id);
				}else{
					$("#inputForm").append('<input type="hidden" name="tmpFileId" value="' + id + '">');
				}
				decideShowUpladBtn('thumb', false);
			});

			// 文件上传失败，现实上传出错。
			uploader2.on( 'uploadError', function( file ) {
				var $li = $( '#'+file.id ),
						$error = $li.find('div.error');

				// 避免重复创建
				if ( !$error.length ) {
					$error = $('<div class="error"></div>').appendTo( $li );
				}

				$error.text('上传失败');
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader2.on( 'uploadComplete', function( file ) {
				$( '#'+file.id ).find('.progress').remove();
			});


			$("#submit_btn").click(function(){
				$("#inputForm").submit();
			});
		});

		function delImg(_this, type){
			var $parent = $(_this).parent();
			var imgId = $parent.attr('imgId');
			var tmpFileId = $parent.attr('tmpFileId');

			if(imgId || tmpFileId){
				if(confirm('确定要删除图片吗？')){
					$.ajax({
						url: '${ctx}/sys/imageSetting/deleteImg',
						data:{
							type: type,
							imgId: imgId,
							tmpFileId: tmpFileId
						},
						type: 'post',
						dataType: 'text',
						success: function(result){
							if(1 == result){
								if('thumb' ==  type){
									if (tmpFileId) {
										$('#' + tmpFileId).remove();
										uploader2.removeFile($parent.attr("id"), true);
									}
									$parent.remove();
									decideShowUpladBtn(type, true);
									$('input[name=tmpFileId]').val("-1");
								}else {
									if (tmpFileId) {
										$('#' + tmpFileId).remove();
										uploader.removeFile($parent.attr("id"), true);
									}
									$parent.remove();
									decideShowUpladBtn();
								}
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

		function decideShowUpladBtn(type, isShow){
			if(isShow){
				$("#filePicker2").show();
			}else{
				$("#filePicker2").hide();
			}
		}
	</script>
</body>
</html>
