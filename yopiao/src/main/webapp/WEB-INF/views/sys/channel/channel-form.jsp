<%@ page import="com.zx.stlife.constant.Const" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑居${channelName}</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/js/webuploader/webuploader.css">
</head>

<body>
	<form:form id="inputForm" modelAttribute="entity" action="${ctx}/sys/channel/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="category"/>
		<fieldset>
			<legend><small>${channelName}管理 > ${empty entity.id ? "新增" : "修改"}${channelName}</small></legend>
			<div id="messageBox" class="alert alert-error controls" style="display:none">输入有误，请先更正。</div>

			<div class="control-group">
				<label class="control-label">名称:</label>
				<div class="controls">
					<form:input path="name" cssClass="span3" placeholder="请输入2-8个字符"/>
				</div>
			</div>

			<c:set var="category"><%=Const.ChannelCategory.JJFW%></c:set>
			<c:if test="${category != entity.category}">
				<div class="control-group">
					<label class="control-label">链接地址:</label>
					<div class="controls">
						<form:input path="url" cssClass="required span5" placeholder="链接地址，以http开头，6-128个字符"/>
					</div>
				</div>
			</c:if>

			<div class="control-group">
				<label class="control-label">序号:</label>
				<div class="controls">
					<form:input path="sortNum" cssClass="required span3" placeholder="请输入1-99内的整数"/>
				</div>
			</div>

			<div class="control-group">
				<label class="control-label">图片<br>100*100像素:</label>
				<div class="controls">
					<div class="wl-container">
						<div id="fileList2" class="uploader-list">
							<c:if test="${not empty entity.thumbImg}">
								<div class="file-item thumbnail" imgId="${entity.id}">
									<img src="${channelImgRootURI}/${entity.thumbImg}" style="width:100px;height: 100px;">
									<div class="del" onclick="delImg(this, 'thumb')">X</div>
								</div>
							</c:if>
						</div>
						<div id="filePicker2" style="display: ${empty entity.thumbImg ? "" : "none"};">选择图片</div>
					</div>
				</div>
			</div>

			<div class="form-actions">
				<input id="submit_btn" class="btn btn-primary" type="button" value="保存"/>&nbsp;
				<input id="btnBack" class="btn" type="button" value="返回" />
			</div>
		</fieldset>
		<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
		<script type="text/javascript" src="${ctx}/static/js/webuploader/webuploader.min.js"></script>
		<script src="${ctx}/static/js/My97DatePicker/WdatePicker.js"></script>
	</form:form>

	<script>
		// Web Uploader实例
		var uploader2;
		$(document).ready(function() {

			//为inputForm注册validate函数
			$("#inputForm").validate({
				rules: {
					name: {
						required: true,
						rangelength: [2, 8]
					},
				<c:if test="${category != entity.category}">
					url: {
						required: true,
						isURL: true,
						rangelength: [6, 128],
					},
				</c:if>
					sortNum: {
						isNumber: true,
						numRange: [1, 99]
					}
				},
				messages: {
					name: {
						rangelength: '请输入2-8个字符'
					},
					<c:if test="${category != entity.category}">
					url: {
						isURL: '链接地址格式不正确',
						rangelength: '请输入6-128个字符',
					},
					</c:if>
					sortNum: {
						isNumber: '请输入1-99内的整数',
						numRange: '请输入1-99内的整数'
					}
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					if( element.is(":checkbox") ) {
						error.appendTo(element.parent().next());
					}else {
						error.insertAfter(element);
					}
				},
				ignore:""
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
					width: 100,
					height: 100,
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
				}, 100, 100 );
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

				var id = response._raw;
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
						url: '${ctx}/sys/channel/deleteImg',
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
