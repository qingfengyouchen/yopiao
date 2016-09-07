<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑模块</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/js/webuploader/webuploader.css">
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						云购管理 > ${empty entity.id ? "新增" : "修改"}云购公告
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/service/activity/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<form:hidden path="type" value="2"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">标题</label>
							<div class="col-sm-6">
								<form:input path="title" cssClass="form-control" placeholder="请输入1-64个英文、数字或下划线" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
							<!-- 
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">公告时间</label>
							<div class="col-sm-6">
								<input type="text" name="activityTime" id="activityTime" class="laydate-icon form-control"   onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})"  value='<fmt:formatDate value="${entity.activityTime}" pattern="YYYY-MM-DD hh:mm:ss" />'/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						 -->
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">活动图:124*190像素</label>
							<div class="col-sm-6">
								<div id="fileList1" class="uploader-list">
									<c:if test="${not empty entity.thumbImgUrl}">
										<div class="file-item thumbnail" tmpImgId="${entity.id}">
											<img src="${activityRootUri}/${entity.thumbImgUrl}" style="width:135px;">
											<div class="del" onclick="delImg(this, 'thumbImgUrl')">X</div>
										</div>
									</c:if>
								</div>
								<div id="filePicker1" style="display: ${empty entity.thumbImgUrl ? "" : "none"};">
									<button class="btn btn-success " type="button"><i class="fa fa-upload"></i>&nbsp;&nbsp;<span class="bold">上传图片</span></button>	
								</div>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
					    <div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">介绍</label>
							<div class="col-sm-10">
								<script id="container" name="content" type="text/plain">${entity.content}</script>
								<jsp:include page="/common/ueditor-lib.jsp" flush="true"></jsp:include>
								<c:set var="maxLength" value="40000"/>
								<script type="text/javascript">
									var ue = UE.getEditor('container', {
										maximumWords:${maxLength},
										initialFrameWidth: '98%'
									});
								</script>
							</div>
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
	<jsp:include page="/common/foot-list-js.jsp" flush="true"></jsp:include>
	<script type="text/javascript" src="${ctx}/static/js/webuploader/webuploader.min.js"></script>
	 <!-- layerDate plugin javascript -->
    <script src="${ctx }/static/mould/js/plugins/layer/laydate/laydate.js"></script>
	
	<script type="text/javascript">
		$(function(){
			  var start = {
				    elem: '#activityTime',
				    format: 'YYYY-MM-DD hh:mm:ss',
				    min: laydate.now(), //设定最小日期为当前日期
				    istime: true,
				    istoday: false,
				    choose: function(datas){
				         end.min = datas; //开始日选好后，重置结束日的最小日期
				         end.start = datas //将结束日的初始值设定为开始日
				    }
				};
				laydate(start);
		});
	
	var uploader1, uploader2;
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
			rules:{
				title:{
					required: true,
                    minlength:1,
                    maxlength:64
				},
				activityTime:{
					required: true
				}
			},
			messages:{
				title: {
                    required: icon + "请输入您的标题",
                    minlength: icon + "标题最少1个字符",
                    maxlength: icon + "标题最多64个字符"
				},
				activityTime:{
					required: icon + "请输入活动时间",
				}
			},
        	errorPlacement:function(error,element) {  
        		error.appendTo(element.parent("div").next("label"));
           }
		});


		var $list1 = $('#fileList1'),
				$list2 = $('#fileList2'),
		// 优化retina, 在retina下这个值是2
				ratio = window.devicePixelRatio || 1;
		// 初始化缩略图Web Uploader
		uploader1 = WebUploader.create({

			// 自动上传。
			auto: true,

			// swf文件路径
			swf: '${ctx}/static/js/webuploader/Uploader.swf',

			// 文件接收服务端。
			server: '${ctx}/tmpFile/upload',

			// 选择文件的按钮。可选。
			// 内部根据当前运行是创建，可能是input元素，也可能是flash.
			pick: '#filePicker1',

			fileNumLimit: 1, //验证文件总数量, 超出则不允许加入队列。

			// 只允许选择文件，可选。
			accept: {
				title: 'Images',
				extensions: 'jpg,jpeg,bmp,png',
				mimeTypes: 'image/*'
			},
			compress: {
				width: 124,
				height: 190,
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
		uploader1.on( 'fileQueued', function( file ) {
			var $li = $(
							'<div id="' + file.id + '" class="file-item thumbnail">' +
							'<img>' +
								//'<div class="info">' + file.name + '</div>' +
							'<div class="del" onclick="delImg(this, \'thumbImgUrl\')">X</div>' +
							'</div>'
					),
					$img = $li.find('img');

			$list1.append( $li );

			// 创建缩略图
			uploader1.makeThumb( file, function( error, src ) {
				if ( error ) {
					$img.replaceWith('<span>不能预览</span>');
					return;
				}

				$img.attr( 'src', src );
			},124, 190 );
		});

		// 文件上传过程中创建进度条实时显示。
		uploader1.on( 'uploadProgress', function( file, percentage ) {
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
		uploader1.on( 'uploadSuccess', function( file, response) {
			var $container = $( '#'+file.id );
			$container.addClass('upload-state-done');
			var id = typeof(response) == 'object' ? response._raw : response;
			var aa=jQuery.parseJSON(file);
			$container.attr("tmpImgId", id);
			var $hiddenFile = $('input[name=thumbImgUrlId]');
			if($hiddenFile.length > 0){
				$hiddenFile.val(id);
			}else{
				$("#inputForm").append('<input type="hidden" name="thumbImgUrlId" value="' + id + '">');
			}
			decideShowUpladBtn('thumbImgUrl', false);
		});

		// 文件上传失败，现实上传出错。
		uploader1.on( 'uploadError', function( file ) {
			var $li = $( '#'+file.id ),
					$error = $li.find('div.error');

			// 避免重复创建
			if ( !$error.length ) {
				$error = $('<div class="error"></div>').appendTo( $li );
			}

			$error.text('上传失败');
		});

		// 完成上传完了，成功或者失败，先删除进度条。
		uploader1.on( 'uploadComplete', function( file ) {
			$( '#'+file.id ).find('.progress').remove();
		});


		// 初始化顶部图片Web Uploader
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
				height: 600,
				// 图片质量，只有type为`image/jpeg`的时候才有效。
				quality: 100,
				// 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
				allowMagnify: false,
				// 是否允许裁剪。
				crop: false,
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
							'<div class="del" onclick="delImg(this, \'imgUrl\')">X</div>' +
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
			}, 750, 600);
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
			$container.attr("tmpImgId", id);
			var $hiddenFile = $('input[name=imgUrlId]');
			if($hiddenFile.length > 0){
				$hiddenFile.val(id);
			}else{
				$("#inputForm").append('<input type="hidden" name="imgUrlId" value="' + id + '">');
			}
			decideShowUpladBtn('imgUrl', false);
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
		var tmpImgId = $parent.attr('tmpImgId');
		if(tmpImgId){
			layer.open({
				title:'温馨提示',
			    content: '确定要删除吗？',
		        shift:6,
		        closeBtn:2 ,
		        icon : 2
			    ,btn: ['确定', '取消',]
			    ,btn1: function(index, layero){
					if('imgUrl' ==  type){
						$.post("${ctx}/service/activity/delImgUrl",{id:tmpImgId},function(state){
							if(state == <%=Const.COMMON_RESULT_SUCCESS%>){
								if (tmpImgId) {
									$('#' + tmpImgId).remove();
									try {
										uploader2.removeFile($parent.attr("id"), true);
									} catch (e) {
									}
								}
								$parent.remove();
								decideShowUpladBtn(type, true);
								$('input[name=imgUrlId]').val("-1");
								layer.msg('删除成功', {icon: 0});
							}else{
								layer.msg('删除成功', {icon: 0});
							}
						});
					}else {
						$.post("${ctx}/service/activity/delThumbImgUrl",{id:tmpImgId},function(state){
							if(state == <%=Const.COMMON_RESULT_SUCCESS%>){
								if (tmpImgId) {
									$('#' + tmpImgId).remove();
									try {
										uploader1.removeFile($parent.attr("id"), true);
									} catch (e) {}
								}
								$parent.remove();
								decideShowUpladBtn(type, true);
								$('input[name=thumbImgUrlId]').val("-1");	
								layer.msg('删除成功', {icon: 0});
							}else{
								layer.msg('删除成功', {icon: 0});
							}
						});
					}	
			    },btn2: function(index){ 
			    	layer.close();
			    }
			});
		}
	}
	
	function decideShowUpladBtn(type, isShow){
		if('thumbImgUrl' == type){
			if(isShow){
				$("#filePicker1").show();
			}else{
				$("#filePicker1").hide();
			}
		}else if("imgUrl" == type){
			if(isShow){
				$("#filePicker2").show();
			}else{
				$("#filePicker2").hide();
			}
		}
	}

		$("#submit_btn").click(function(){

			var txt = ue.getContentTxt();
			if(txt.length > ${maxLength}){
				alert("你输入的字符个数已经超出最大允许值！");
				return;
			}
			ue.sync('inputForm');

			$("#inputForm").submit();
		});
	</script>
</body>
</html>
