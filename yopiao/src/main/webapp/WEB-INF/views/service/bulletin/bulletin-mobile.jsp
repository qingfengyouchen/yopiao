<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
		<title>E购活动</title>
		<link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/style.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/global.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/index.css" />
	</head>
    <body>
       <div class="max-box">
          <!-- 头部结束 -->
          <div class="main">
            <ul class="main_can">
            	<c:forEach items="${activities}" var="activitie">
	              <a href=" ${staticHtmlRootUri }/${nodeActivity}/${activitie.htmlUrl }" target="_self" >
	                 <li class="line">
	                    <div class="can_pic"><img src="${activityRootUri}/${activitie.thumbImgUrl}"></div>
	                    <div class="can">
	                      <p class="lar_l blank plr te">${ activitie.title}</p>
	                      <p class="large text-low2 plr">${ activitie.maxJoinAmount}人/ <fmt:formatDate  value="${ activitie.activityTime}" type="both" pattern="yyyy年MM月dd日 HH时mm分" /> /地点： ${ activitie.address}</p>
	                    </div>
	                 </li>
	              </a>            	 	
            	</c:forEach>
            </ul>
          </div>
       </div>
    </body>

</html>

