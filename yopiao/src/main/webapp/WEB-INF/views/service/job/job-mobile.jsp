<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
		<title>求职</title>
		<link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/style.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/global.css" />
		<link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/index.css" />
	</head>
    <body>
       <div class="max-box">
          <!-- 头部结束 -->
          <div class="tab">
             <ul>
               <li class="p-line-h tab-p"><a href="${ctx }/service/recruitment/selListRecruitment" target="_self" class="block">招聘</a></li>
               <li class="p-line primary">求职</li>
             </ul>
          </div>
          <!-- 菜单栏结束 -->
          <div class="main">
            <ul class="main-ul">
            	<c:forEach items="${jobs }" var="job">
	              <a href="${staticHtmlRootUri }/${nodeJob}/${job.htmlUrl}" target="_self">
	                  <li class="line">
	                   <dl class="conten">
	                     <dt class="largest_plus text-heavy">${job.title }</dt>
	                     <dd class="normal text-low2">${gender[job.gender]}，${job.age}岁，${job.education }，工作经验：${job.workExperience }，现居：${job.apartmentAddr }</dd>
	                   </dl>
	                  </li>
	              </a>            		
            	</c:forEach>
            </ul>
          </div>
       </div>
    </body>

</html>