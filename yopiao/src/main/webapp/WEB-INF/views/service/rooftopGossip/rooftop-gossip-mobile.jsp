<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
		<title>E购八卦</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/style.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/global.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/index.css" />
    </head>
	<body>
	<div class="max-box">
          <!-- 菜单栏结束 -->
          <div class="main">
            <ul class="main_in">
	             <c:forEach items="${page.result}" var="entity">
	              <a href="${staticHtmlRootUri}/${rooftopGossip}/${ entity.htmlUrl}" target="_self" >
	                 <li class="line clearfloat">	
	                 	<input type="hidden" name="ids" value="${entity.id}"/>
	                    <p class="cp lar_l blank plr te">${entity.title}</p>
	                 </li>
	              </a>
             	 </c:forEach>
            </ul>
          </div>
       </div>
</body>
</html>