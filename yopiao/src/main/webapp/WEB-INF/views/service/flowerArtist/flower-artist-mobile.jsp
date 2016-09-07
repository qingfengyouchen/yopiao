<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
		<title>鲜花速递</title>
		<link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/style.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/global.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/index.css" />
	</head>
    <body>
       <div class="max-box">
          <!-- 头部结束 -->
          <div class="main">
            <ul class="main_f">
            	<c:forEach items="${flowerArtists }" var="flowerArtist">
	              <li class="line clearfloat">
	                    <a href="${staticHtmlRootUri}/${nodeFlowerartist }/${ flowerArtist.htmlUrl}" target="_self" >
	                      <div class="con">
	                        <p class="cp lar_l blank">${flowerArtist.title }</p>
	                        <p class="large text-low2">${flowerArtist.sendRange }</p>
	                      </div>
	                   </a>
	                   <div class="con_tel"><a href="tel://${flowerArtist.tel }"></a></div>
	              </li>            		
            	</c:forEach>	
            </ul>
          </div>
       </div>
    </body>
</html>