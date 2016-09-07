<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<html>
<head>
	<title>设置中奖用户</title>
	<jsp:include page="/common/head-css.jsp" flush="true"></jsp:include>
	<jsp:include page="/common/foot-form-js.jsp" flush="true"></jsp:include>
</head>

<body>
	<div class="row">
		<div class="col-sm-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					<form:form id="inputForm" modelAttribute="entity"  method="post" class="form-horizontal">
						<form:hidden path="id"/>
						<div class="form-group">
							<label class="col-sm-2 control-label">期号:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.times}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">商品名称:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.goodsName}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>

						<div class="form-group">
							<label class="col-sm-2 control-label">已购买人次/总需人次:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.totalBuyTimes} / ${entity.totalTimes}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>

						<div class="form-group">
							<label class="col-sm-2 control-label">夺宝进度:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.snatchProgress}%</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>

						<c:set var="hasPresetWinner" value="${not empty entity.presetWinnerUserId}"></c:set>
						<c:if test="${hasPresetWinner}">
						<div class="form-group" style="color: blue;">
							<label class="col-sm-2 control-label">预设中奖用户名:</label>
							<div class="col-sm-6">
								<label class="col-sm control-label">${entity.presetWinnerUserName}</label>
							</div>
						</div>
						<div class="hr-line-dashed"></div>
						</c:if>

						<c:set var="overState"><%=Const.GoodsTimesState.OVER%></c:set>
						<c:set var="isOver" value="${overState==entity.state}"></c:set>
						<c:choose>
						<c:when test="${entity.snatchProgress == 100}">
							<div class="form-group">
								<label class="col-sm-2 control-label">揭晓时间:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label"><fmt:formatDate value="${entity.openTime}" pattern="yyyy-MM-dd HH:mm:ss"/></label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>

							<div class="form-group">
								<label class="col-sm-2 control-label">后台开奖时间:</label>
								<div class="col-sm-6">
									<label class="col-sm control-label">
										<span style="color:red;font-weight: bold;" id="openTime"></span>
									</label>
								</div>
							</div>
							<div class="hr-line-dashed"></div>

							<c:choose>
								<c:when test="${isOver}">
									<div class="form-group">
										<label class="col-sm-2 control-label">状态:</label>
										<div class="col-sm-6">
											<label class="col-sm control-label" style="color: red;">
												已揭晓
											</label>
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<div class="form-group">
										<label class="col-sm-2 control-label">后台开奖倒计时:</label>
										<div class="col-sm-6">
											<label class="col-sm control-label">
												<span style="color:red;font-weight: bold; font-size: 20px;" id="openTimeCounter"></span>
											</label>
										</div>
									</div>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>

                            <c:if test="${!isOver}">
                            <!-- jqgrid-->
                            <link href="${ctx}/static/mould/css/plugins/jqgrid/ui.jqgrid.css" rel="stylesheet">
                            <script type="text/javascript" src="${ctx}/static/mould/js/plugins/jqgrid/i18n/grid.locale-cn.js"></script>
                            <script type="text/javascript" src="${ctx}/static/mould/js/plugins/jqgrid/jquery.jqGrid.min.js"></script>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">选择中奖用户:</label>
                                <div class="col-sm-10">
                                    <table id="jqGrid"></table>
                                    <div id="jqGridPager"></div>
                                </div>
                            </div>
                            <div class="hr-line-dashed"></div>

                            <script type="text/javascript">

                                $(document).ready(function () {
                                    $("#jqGrid").jqGrid({
                                        url: '${ctx}/goods/goodsTimes/getSnatchRecord/${entity.id}',
                                        mtype: "GET",
                                        datatype: "json",
                                        styleUI : 'Bootstrap',
                                        rowNum:20,
                                        rowList:[20, 30, 50],
                                        colModel: [
                                            { label: '用户ID', name: 'userId', width: 160, key: true, align: 'center', sorttype:'integer'},
                                            { label: '用户名', name: 'userName', align: 'center', width: 140},
                                            { label: '手机号码', name: 'mobileNo', align: 'center', width: 160},
                                            { label: '用户昵称', name: 'nickName', align: 'center', width: 200},
                                            { label: '购买人次', name: 'buyTimes', align: 'center', width: 140, sorttype:'integer'}
                                        ],
                                        loadonce: true,
                                        viewrecords: true,
                                        width: 800,
                                        height: 250,
                                        rowNum: 10,
                                        pager: "#jqGridPager"
                                    });
                                    // activate the build in search with multiple option
                                    $('#jqGrid').navGrid("#jqGridPager", {
                                                search: true, // show search button on the toolbar
                                                add: false,
                                                edit: false,
                                                del: false,
                                                refresh: true
                                            },
                                            {}, // edit options
                                            {}, // add options
                                            {}, // delete options
                                            { multipleSearch: true, multipleGroup: true, showQuery: true } // search options - define multiple search
                                    );
                                });

                                function getSelectedRow() {
                                    var grid = $("#jqGrid");
                                    var rowKey = grid.jqGrid('getGridParam',"selrow");
                                    return rowKey;
                                }
                            </script>
                            </c:if>
							<div class="form-group">
								<label class="col-sm-2 control-label">设置虚拟用户:</label>
								<div class="col-sm-5">
									<input type="text" id="userAmount" name="userAmount" placeholder="请输入1-${maxUser}内的整数"
										   autocomplete="off" style="width: 140px;text-align: right;" value="${buyAllParams.userAmount}"/>人，
									<input type="text" id="takeTime" name="takeTime" placeholder="请输入正整数"
										   autocomplete="off" style="width: 140px;text-align: right;" value="${buyAllParams.takeTime}"/>分钟买满
									<div style="color:deeppink;">(备注：设置的买满时间可能会存在误差，无法保证100%准时)</div>
								</div>
								<label class="col-sm-4 control-label hint" style="text-align: left;"> </label>
							</div>
							<div class="hr-line-dashed"></div>
						</c:otherwise>
						</c:choose>

						<div class="form-group">
							<div class="col-sm-6 col-sm-offset-2">
								<c:if test="${!isOver}">
								<button class="btn btn-primary" type="button" onclick="setWinner()" id="btnSetWinner">
									${hasPresetWinner ? "修改" : "设置"}中奖用户
								</button>
								</c:if>
								<button class="btn btn-primary" type="button" onclick="buyAll()" id="btnBuyAll">买满</button>

								<button class="btn btn-success" onclick="refresh()" type="button">刷新</button>

								<button class="btn btn-success" id="btnBack" type="button">返回</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctx}/static/js/utils/moment-with-locales.js"></script>
<script type="text/javascript">
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
		errorClass: "m-b-none",
		validClass: "m-b-none"
	});

	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#inputForm").validate({
		rules: {
			userAmount: {
				required: true,
				isNumber: true,
				numRange: [1, ${maxUser}]
			},
			takeTime: {
				required: true,
				isNumber: true
			}
		},
		messages: {
			userAmount: {
				isNumber: '请输入正整数',
				numRange: "请输入1-${maxUser}内的正整数"
			},
			takeTime: {
				isNumber: '请输入正整数'
			}
		},
		errorPlacement:function(error,element) {
			error.appendTo(element.parent("div").next("label"));
		}
	});

	function buyAll(){
			var index;
			var takeTime = $('#takeTime').val();
			$.ajax({
				url: '${ctx}/goods/goodsTimes/buyAll',
				data:{
					goodsTimesId: $('#id').val(),
					userAmount: $('#userAmount').val(),
					takeTime: takeTime
				},
				type: 'post',
				dataType: 'text',
				beforeSend: function(){
					index = layer.load();
				},
				success: function(result){
					alert("tes");
					layer.close(index);
					if(result == "1"){
						Public.alert("设置成功，请稍等...");
						var deadlineDate = moment().add(takeTime, 'm');
						buyAllCounter(deadlineDate);
					}else if(result == "2"){
						Public.alert("已设置，不需要重复设置");
					}else if(result == "3"){
						Public.alert("非进行状态不能买满");
					}else if(result == "4"){
						Public.alert("用户数量设置有误，请合理设置用户数量");
					}else if(result == "5"){
						Public.alert("不允许买满虚拟用户，操作错误");
					}else{
						Public.alert("设置失败：" + result);
					}
				},
				error: function(){
					//alert("tes");
					layer.close(index);
					Public.alert("设置失败");
				}
			});
	}

	function buyAllCounter(deadlineDate){
		var t = setInterval(function(){
			var ms = deadlineDate.diff(moment());
			if(ms < 1){
				clearInterval(t);
				$('#btnBuyAll').html('<span style="color: red; font-weight: bold;">已经买满...</span>');
			}else{
				var content = moment.utc(ms).format("mm:ss:SSS");
				$('#btnBuyAll').removeClass('btn-primary')
						.addClass('btn-default')
						.html('距离买满还有: <span style="color: red; font-weight: bold;">' + content + '</span>');
				$('#btnBuyAll').attr('disabled', true);
			}
		}, Public.getRandomNum(100));
	}

	<c:choose>
	<c:when test="${not empty buyAllParams}">
		var deadlineDate = moment('<fmt:formatDate value="${buyAllParams.fullTime}" pattern="yyyy-MM-dd HH:mm:ss"/>' , "YYYY-MM-DD HH:mm:ss");
		buyAllCounter(deadlineDate);
	</c:when>
	<c:when test="${entity.snatchProgress == 100}">
		var deadlineDate = moment('<fmt:formatDate value="${entity.openTime}" pattern="yyyy-MM-dd HH:mm:ss"/>', "YYYY-MM-DD HH:mm:ss");
		deadlineDate.subtract(2, 'm');
		$('#openTime').text(deadlineDate.format("YYYY-MM-DD HH:mm:ss"));
		<c:if test="${!isOver}">
		var t = setInterval(function(){
			var ms = deadlineDate.diff(moment());
			var content;
			if(ms < 1){
				clearInterval(t);
				content = "等待揭晓";
				$('#btnSetWinner').attr('disabled', true);
				$('#btnSetWinner').removeClass('btn-primary')
						.addClass('btn-default')
						.html('等待揭晓...');

			}else{
				content = moment.utc(ms).format("mm:ss:SSS");
			}
			$('#openTimeCounter').html(content);
		}, Public.getRandomNum(100));

		
		</c:if>
	</c:when>
	</c:choose>

	function refresh(){
		location.reload(true);
	}
	function setWinner(){
        var userId = getSelectedRow();
        if(Public.isNull(userId)){
            Public.alert('请选择用户');
            return;
        }

        var index;
        $.ajax({
            url: '${ctx}/goods/goodsTimes/setWinner',
            data:{
                goodsTimesId: $('#id').val(),
                userId: userId
            },
            type: 'post',
            dataType: 'text',
            beforeSend: function(){
                index = layer.load();
            },
            success: function(result){
                layer.close(index);
                if(result == "1"){
                    Public.alert("设置成功，请稍等...");
                    setTimeout(function(){
                        refresh();
                    }, 3000);
                    refresh();
                }else if(result == "2"){
                    Public.alert("不能设置已揭晓的期号");
                }else{
                    Public.alert("设置失败：" + result);
                }
            },
            error: function(){
                layer.close(index);
                Public.alert("设置失败");
            }
        });
    }
</script>
</body>
</html>
