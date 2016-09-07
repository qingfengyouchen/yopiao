<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<c:forEach items="${page.result}" var="entity">
    <tr>
        <td style="text-align: center;"><img src="${goodsRootUri}/${entity.thumbnail}"
                                             style="width: 108px; height: 80px;"></td>
        <td style="text-align: left;">
            ${entity.goodsName}
        </td>
        <td style="text-align: center;">${entity.times}&nbsp;</td>
        <td style="text-align: center;">
            <div class="progress progress-striped ${entity.snatchProgress == 100 && entity.state!=waittingState ? "" : "active"}" style="margin-bottom: 0px;">
                <div style="width: ${entity.snatchProgress}%" aria-valuemax="100" aria-valuemin="0"
                     aria-valuenow="${entity.snatchProgress}" role="progressbar"
                     class="progress-bar progress-bar-${entity.snatchProgress == 100 ? (entity.state==waittingState ? "danger" : "success") : "primary"}">
                    <span style="${entity.snatchProgress > 19 ? "" : "color: #5e5e5e;"}">${entity.snatchProgress}%</span>
                </div>
            </div>
        </td>
        <td style="text-align: center;">${entity.totalBuyTimes} / ${entity.totalTimes}</td>
        <td style="text-align: center;">${goodsTimesStateMap[entity.state]}</td>
        <td style="text-align: center;">
		    <span ${waittingState == entity.state ? "class=\"code_counter\"" : ""}>
				<fmt:formatDate value="${entity.openTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</span>
        </td>
        <td style="text-align: center;">
            <span style="${empty entity.presetWinnerUserId ? "" : "color:red;"}">
            ${empty entity.presetWinnerUserId ? "否" : "是"}</span>
        </td>
        <td style="text-align: center;">
            <shiro:hasPermission name="goods:goodsTimes:settingDraw">
                <c:if test="${entity.state <= waittingState}">
                    <a class="btn btn-success aedit" href="javascript:;" id="${entity.id }" style="margin-top: 2px;"><i class="fa fa-magic"></i> 设置中奖</a>
                </c:if>
            </shiro:hasPermission>
        </td>
    </tr>
</c:forEach>
<tr>
    <td colspan="10" style="text-align: right;">
        <jsp:include page="/common/ajax-page.jsp"></jsp:include>
    </td>
</tr>

<script type="text/javascript">
    $(function(){
        $('.code_counter').each(function(i, e){
            var $e = $(e);
            var timeStr = $.trim($e.text());
            if(Public.isNotNull(timeStr)){
                var deadlineDate = moment(timeStr, "YYYY-MM-DD HH:mm:ss");
                deadlineDate.subtract(2, 'm');
                $e.css({
                    "color": "red",
                    "font-size": "20px"
                });
                var t = setInterval(function(){
                    var ms = deadlineDate.diff(moment());
                    var content;
                    if(ms < 1){
                        clearInterval(t);
                        content = "等待揭晓";
                    }else{
                        content = moment.utc(ms).format("mm:ss:SS");
                    }
                    $e.html(content);
                }, 30);
            }
        });
    });
</script>