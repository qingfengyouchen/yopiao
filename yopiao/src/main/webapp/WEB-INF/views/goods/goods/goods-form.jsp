<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>编辑商品</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/js/webuploader/webuploader.css">
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						商品管理 > ${empty entity.id ? "新增" : "修改"}商品
					</h5>
				</div>
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity" action="${ctx}/goods/goods/save" method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<input type="hidden" name="params[oldState]" value="${entity.state}">
						<input type="hidden" name="params[name]" value="${entity.name}">
						<div class="form-group">
							<label class="col-sm-2 control-label">商品类别</label>
							<div class="col-sm-6">
								<form:select path="goodsCategoryId" cssClass="form-control">
									<form:option value="">请选择</form:option>
									<form:options items="${goodsCategoryList}" itemValue="id" itemLabel="name"/>
								</form:select>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>

						<div class="form-group">
							<label class="col-sm-2 control-label">商品名称</label>
							<div class="col-sm-6">
								<form:input path="name" cssClass="form-control" placeholder="请输入6-64个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>

						<div class="form-group">
							<label class="col-sm-2 control-label">提示信息</label>
							<div class="col-sm-6">
								<form:input path="tip" cssClass="form-control" placeholder="请输入0-64个字符" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>

						<div class="form-group">
							<label class="col-sm-2 control-label">总需购买人次</label>
							<div class="col-sm-6">
								<form:input path="totalTimes" cssClass="form-control" placeholder="请输入1-1000000内的正整数" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						
						<div class="form-group">
							<label class="col-sm-2 control-label">兑换积分</label>
							<div class="col-sm-6">
								<form:input path="changeJifen" cssClass="form-control" placeholder="请输入该商品可兑换的积分数量" autocomplete="off"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>						
						
						<div class="hr-line-dashed"></div>

						<div class="form-group">
							<label class="col-sm-2 control-label">是否10元夺宝</label>
							<div class="col-sm-6">
								<form:bsradiobuttons path="isTenYuan" items="${booleanMap}" labelCssClass="radio-inline"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>

						<div class="form-group">
							<label class="col-sm-2 control-label">状态</label>
							<div class="col-sm-6">
								<form:bsradiobuttons path="state" items="${goodsStateMap}" labelCssClass="radio-inline"/>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>

						<div class="form-group">
							<label class="col-sm-2 control-label">缩略图:270*200像素</label>
							<div class="col-sm-6">
								<div id="fileList1" class="uploader-list">
									<c:if test="${not empty entity.thumbnail}">
										<div class="file-item thumbnail" imgId="${entity.id}">
											<img src="${goodsRootUri}/${entity.thumbnail}" style="width:135px;">
											<div class="del" onclick="delImg(this, 'thumb')">X</div>
										</div>
									</c:if>
								</div>
								<c:if test="${!canNotEdit}">
									<div id="filePicker1" style="display: ${empty entity.thumbnail ? "" : "none"};">
										<button class="btn btn-success " type="button"><i class="fa fa-upload"></i>&nbsp;&nbsp;<span class="bold">上传图片</span></button>	
									</div>
								</c:if>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">顶部切换图片:750*360像素(最多上传8张)</label>
							<div class="col-sm-6">
								<div id="filePicker2">
									<button class="btn btn-success " type="button"><i class="fa fa-upload"></i>&nbsp;&nbsp;<span class="bold">上传图片</span></button>	
								</div>
								<div id="fileList2" class="uploader-list">
									<c:forEach items="${topSwitchImageList}" var="image">
								        <c:if test="${not empty image.url && !fn:startsWith(image.url, 'http')}">
										<div class="file-item thumbnail" imgId="${image.id}">
											<img src="${goodsRootUri}/${image.url}" style="width:135px;">
											<div class="del" onclick="delImg(this, 'top')">X</div>
										</div>
										</c:if>
									</c:forEach>
									</div>
								</div>
							</div>
							<!--  
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
							-->
								<div class="form-group">
										<label class="col-sm-2 control-label">顶部切换图片1的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="topSwitchUrl_1" id="topSwitchUrl_1"  class="form-control" value="${topSwitchUrl_1}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">顶部切换图片2的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="topSwitchUrl_2" id="topSwitchUrl_2"  class="form-control" value="${topSwitchUrl_2}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">顶部切换图片3的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="topSwitchUrl_3" id="topSwitchUrl_3"  class="form-control" value="${topSwitchUrl_3}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">顶部切换图片4的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="topSwitchUrl_4" id="topSwitchUrl_4"  class="form-control" value="${topSwitchUrl_4}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">顶部切换图片5的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="topSwitchUrl_5" id="topSwitchUrl_5" class="form-control" value="${topSwitchUrl_5}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">顶部切换图片6的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="topSwitchUrl_6" id="topSwitchUrl_6" class="form-control" value="${topSwitchUrl_6}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">顶部切换图片7的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="topSwitchUrl_7" id="topSwitchUrl_7"  class="form-control" value="${topSwitchUrl_7}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">顶部切换图片8的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="topSwitchUrl_8" id="topSwitchUrl_8"  class="form-control" value="${topSwitchUrl_8}" placeholder="例如：http://"/>
									</div>
						</div>
						<div class="hr-line-dashed"></div>
						
						
						<div class="form-group">
							<label class="col-sm-2 control-label">图文详情图片:(最多上传8张)</label>
							<div class="col-sm-6">
								<div id="filePicker3">
									<button class="btn btn-success " type="button"><i class="fa fa-upload"></i>&nbsp;&nbsp;<span class="bold">上传图片</span></button>	
								</div>
								<div id="fileList3" class="uploader-list">
									<c:forEach items="${detailsImageList}" var="image">
									    <c:if test="${not empty image.url && !fn:startsWith(image.url, 'http')}">
										<div class="file-item thumbnail" imgId="${image.id}">
											<img src="${goodsRootUri}/${image.url}" style="width:135px;">
											<div class="del" onclick="delImg(this, 'details')">X</div>
										</div>
										</c:if>
									</c:forEach>
								</div>
							</div>
							<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
						</div>
						<div class="form-group">
										<label class="col-sm-2 control-label">图文详情图片1的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="detailsUrl_1" id="detailsUrl_1"  class="form-control" value="${detailsUrl_1}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">图文详情图片2的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="detailsUrl_2" id="detailsUrl_2"   class="form-control" value="${detailsUrl_2}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">图文详情图片3的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="detailsUrl_3" id="detailsUrl_3"  class="form-control" value="${detailsUrl_3}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">图文详情图片4的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="detailsUrl_4" id="detailsUrl_4"  class="form-control" value="${detailsUrl_4}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">图文详情图片5的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="detailsUrl_5" id="detailsUrl_5"  class="form-control" value="${detailsUrl_5}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">图文详情图片6的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="detailsUrl_6" id="detailsUrl_6"  class="form-control" value="${detailsUrl_6}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">图文详情图片7的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="detailsUrl_7" id="detailsUrl_7"  class="form-control" value="${detailsUrl_7}" placeholder="例如：http://"/>
									</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">图文详情图片8的地址：</label>
										<div class="col-sm-6">
											<input type="text" name="detailsUrl_8" id="detailsUrl_8"  class="form-control" value="${detailsUrl_8}" placeholder="例如：http://"/>
									</div>
									</div>
						<div class="hr-line-dashed"></div>
			
		
						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-2">
								<button class="btn btn-primary" type="button" id="submit_btn">保存内容</button>
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
		var uploader1, uploader2, uploader3;
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
						rangelength: [6, 64],
						required: true,
						remote: "${ctx}/goods/goods/checkName?oldName=" + encodeURIComponent('${entity.name}')
					},
					tip: {
						rangelength: [0, 64]
					},
					totalTimes: {
						isNumber: true,
						numRange: [1, 1000000]
					},
					changeJifen: {
						isNumber: true,
						numRange: [1, 1000000]
					}
				},
				messages: {
					name: {
						rangelength: "请输入6-64个字符",
						remote: "名称已存在"
					},
					tip: {
						rangelength: "最多输入64个字符"
					},
					totalTimes: {
						numRange: '请输入1-1000000内的正整数'
					},
					changeJifen: {
						numRange: '请输入1-1000000内的正整数'
					}
				},
	        	errorPlacement:function(error,element) {  
	        		error.appendTo(element.parent("div").next("label"));
	           }
			});

			var $list1 = $('#fileList1'),
					$list2 = $('#fileList2'),
					$list3 = $('#fileList3'),
			// 优化retina, 在retina下这个值是2
					ratio = window.devicePixelRatio || 1,

			// 缩略图大小
					thumbnailWidth = 54,
					thumbnailHeight = 96;

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
					width: 270,
					height: 200,
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
								'<div class="del" onclick="delImg(this, \'thumb\')">X</div>' +
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
				}, 135, 100 );
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
				$container.attr("tmpImgId", id);
				var $hiddenFile = $('input[name=tmpThumbId]');
				if($hiddenFile.length > 0){
					$hiddenFile.val(id);
				}else{
					$("#inputForm").append('<input type="hidden" name="tmpThumbId" value="' + id + '">');
				}
				decideShowUpladBtn('thumb', false);
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

				fileNumLimit: 8, //验证文件总数量, 超出则不允许加入队列。

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
								'<div class="del" onclick="delImg(this, \'top\')">X</div>' +
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
				}, 150, 72);
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
				$("#inputForm").append('<input type="hidden" id="' +
						id + '" name="tmpTopIds" value="' + id + '">');

				decideShowUpladBtn('top', false);
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


			// 初始化图文详情图片Web Uploader
			uploader3 = WebUploader.create({

				// 自动上传。
				auto: true,

				// swf文件路径
				swf: '${ctx}/static/js/webuploader/Uploader.swf',

				// 文件接收服务端。
				server: '${ctx}/tmpFile/upload',

				// 选择文件的按钮。可选。
				// 内部根据当前运行是创建，可能是input元素，也可能是flash.
				pick: '#filePicker3',

				fileNumLimit: 8, //验证文件总数量, 超出则不允许加入队列。

				// 只允许选择文件，可选。
				accept: {
					title: 'Images',
					extensions: 'jpg,jpeg,bmp,png',
					mimeTypes: 'image/*'
				},
				compress: {
					width: 1080,
					height: 10000,
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
			uploader3.on( 'fileQueued', function( file ) {
				var $li = $(
								'<div id="' + file.id + '" class="file-item thumbnail">' +
								'<img>' +
									//'<div class="info">' + file.name + '</div>' +
								'<div class="del" onclick="delImg(this, \'details\')">X</div>' +
								'</div>'
						),
						$img = $li.find('img');

				$list3.append( $li );

				// 创建缩略图
				uploader3.makeThumb( file, function( error, src ) {
					if ( error ) {
						$img.replaceWith('<span>不能预览</span>');
						return;
					}

					$img.attr( 'src', src );
				}, 108, 200);
			});

			// 文件上传过程中创建进度条实时显示。
			uploader3.on( 'uploadProgress', function( file, percentage ) {
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
			uploader3.on( 'uploadSuccess', function( file, response) {
				var $container = $( '#'+file.id );
				$container.addClass('upload-state-done');

				var id = typeof(response) == 'object' ? response._raw : response;
				$container.attr("tmpImgId", id);
				$("#inputForm").append('<input type="hidden" id="' +
						id + '" name="tmpDetailIds" value="' + id + '">');

				decideShowUpladBtn('details', false);
			});

			// 文件上传失败，现实上传出错。
			uploader3.on( 'uploadError', function( file ) {
				var $li = $( '#'+file.id ),
						$error = $li.find('div.error');

				// 避免重复创建
				if ( !$error.length ) {
					$error = $('<div class="error"></div>').appendTo( $li );
				}

				$error.text('上传失败');
			});

			// 完成上传完了，成功或者失败，先删除进度条。
			uploader3.on( 'uploadComplete', function( file ) {
				$( '#'+file.id ).find('.progress').remove();
			});


			$("#submit_btn").click(function(){
				var imgAmount = computeThumbAmount();
				if(imgAmount == 0){
					alert('请上传缩略图');
					return;
				}
				if( imgAmount != 1){
					alert("最多上传1张缩略图");
					return;
				}

				imgAmount = computeTopSwitchAmount();
				if(imgAmount < 0){
				 	alert('请上传顶部切换图片');
				 	return;
				}
				if( imgAmount > 8){
					alert("最多上传8张顶部切换图片");
					return;
				}

				imgAmount = computeDetailsAmount();
				if(imgAmount < 0){
					alert('请上传图文详情图片');
					return;
				}
				if( imgAmount > 8){
					alert("最多上传8张图文详情图片");
					return;
				}
				
				
				var topSwitchUrl_1 = $("#topSwitchUrl_1").val();
				if (topSwitchUrl_1 != "" ) {
					$("#inputForm").append('<input type="hidden" id="11" name="topSwitchUrl" value="' + topSwitchUrl_1 + '">');
				}
				var topSwitchUrl_2 = $("#topSwitchUrl_2").val();
				if (topSwitchUrl_2 != "" && topSwitchUrl_2 != null) {
					$("#inputForm").append('<input type="hidden" id="12" name="topSwitchUrl" value="' + topSwitchUrl_2 + '">');
				}
				var topSwitchUrl_3 = $("#topSwitchUrl_3").val();
				if (topSwitchUrl_3 != "" && topSwitchUrl_3 != null) {
					$("#inputForm").append('<input type="hidden" id="13" name="topSwitchUrl" value="' + topSwitchUrl_3 + '">');
				}
				var topSwitchUrl_4 = $("#topSwitchUrl_4").val();
				if (topSwitchUrl_4 != "" && topSwitchUrl_4 != null) {
					$("#inputForm").append('<input type="hidden" id="14" name="topSwitchUrl" value="' + topSwitchUrl_4 + '">');
				}
				var topSwitchUrl_5 = $("#topSwitchUrl_5").val();
				if (topSwitchUrl_5 != "" && topSwitchUrl_5 != null) {
					$("#inputForm").append('<input type="hidden" id="15" name="topSwitchUrl" value="' + topSwitchUrl_5 + '">');
				}
				var topSwitchUrl_6 = $("#topSwitchUrl_6").val();
				if (topSwitchUrl_6 != "" && topSwitchUrl_6 != null) {
					$("#inputForm").append('<input type="hidden" id="16" name="topSwitchUrl" value="' + topSwitchUrl_6 + '">');
				}
				var topSwitchUrl_7 = $("#topSwitchUrl_7").val();
				if (topSwitchUrl_7 != "" && topSwitchUrl_7 != null) {
					$("#inputForm").append('<input type="hidden" id="17" name="topSwitchUrl" value="' + topSwitchUrl_7 + '">');
				}
				var topSwitchUrl_8 = $("#topSwitchUrl_8").val();
				if (topSwitchUrl_8 != "" && topSwitchUrl_8 != null) {
					$("#inputForm").append('<input type="hidden" id="18" name="topSwitchUrl" value="' + topSwitchUrl_8 + '">');
				}
				
				var detailsUrl_1 = $("#detailsUrl_1").val();
				if (detailsUrl_1 != "" ) {
					$("#inputForm").append('<input type="hidden" id="21" name="detailsUrl" value="' + detailsUrl_1 + '">');
				}
				var detailsUrl_2 = $("#detailsUrl_2").val();
				if (detailsUrl_2 != "" && detailsUrl_2 != null) {
					$("#inputForm").append('<input type="hidden" id="22" name="detailsUrl" value="' + detailsUrl_2 + '">');
				}
				var detailsUrl_3 = $("#detailsUrl_3").val();
				if (detailsUrl_3 != "" && detailsUrl_3 != null) {
					$("#inputForm").append('<input type="hidden" id="23" name="detailsUrl" value="' + detailsUrl_3 + '">');
				}
				var detailsUrl_4 = $("#detailsUrl_4").val();
				if (detailsUrl_4 != "" && detailsUrl_4 != null) {
					$("#inputForm").append('<input type="hidden" id="24" name="detailsUrl" value="' + detailsUrl_4 + '">');
				}
				var detailsUrl_5 = $("#detailsUrl_5").val();
				if (detailsUrl_5 != "" && detailsUrl_5 != null) {
					$("#inputForm").append('<input type="hidden" id="25" name="detailsUrl" value="' + detailsUrl_5 + '">');
				}
				var detailsUrl_6 = $("#detailsUrl_6").val();
				if (detailsUrl_6 != "" && detailsUrl_6 != null) {
					$("#inputForm").append('<input type="hidden" id="26" name="detailsUrl" value="' + detailsUrl_6 + '">');
				}
				var detailsUrl_7 = $("#detailsUrl_7").val();
				if (detailsUrl_7 != "" && detailsUrl_7 != null) {
					$("#inputForm").append('<input type="hidden" id="27" name="detailsUrl" value="' + detailsUrl_7 + '">');
				}
				var detailsUrl_8 = $("#detailsUrl_8").val();
				if (detailsUrl_8 != "" && detailsUrl_8 != null) {
					$("#inputForm").append('<input type="hidden" id="28" name="detailsUrl" value="' + detailsUrl_8 + '">');
				}

				$("#inputForm").submit();
			});
		});

		function delImg(_this, type){
			var $parent = $(_this).parent();
			var imgId = $parent.attr('imgId');
			var tmpImgId = $parent.attr('tmpImgId');

			if(imgId || tmpImgId){
				if(confirm('确定要删除图片吗？')){
					$.ajax({
						url: '${ctx}/goods/goods/deleteImg',
						data:{
							type: type,
							imgId: imgId,
							tmpImgId: tmpImgId
						},
						type: 'post',
						dataType: 'text',
						success: function(result){
							if(1 == result){
								if('thumb' ==  type){
									if (tmpImgId) {
										$('#' + tmpImgId).remove();
										uploader1.removeFile($parent.attr("id"), true);
									}
									$parent.remove();
									decideShowUpladBtn(type, true);
									$('input[name=tmpThumbId]').val("-1");
								}else {
									if (tmpImgId) {
										$('#' + tmpImgId).remove();
										if('top' ==  type) {
											uploader2.removeFile($parent.attr("id"), true);
										}else if('details' == type){
											uploader3.removeFile($parent.attr("id"), true);
										}
									}
									$parent.remove();
									decideShowUpladBtn(type);
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
			if('thumb' == type){
				if(isShow){
					$("#filePicker1").show();
				}else{
					$("#filePicker1").hide();
				}
			}else if("top" == type){
				var $btn = $("#filePicker2");
				if (computeTopSwitchAmount() >= 8) {
					$btn.hide();
				} else {
					$btn.show();
				}
			}else if("details" == type){
				var $btn = $("#filePicker3");
				if (computeDetailsAmount() >= 8) {
					$btn.hide();
				} else {
					$btn.show();
				}
			}
		}

		function computeThumbAmount(){
			return $("#fileList1>div.file-item").length;
		}
		function computeTopSwitchAmount(){
			return $("#fileList2>div.file-item").length;
		}
		function computeDetailsAmount(){
			return $("#fileList3>div.file-item").length;
		}
	</script>
</body>
</html>
