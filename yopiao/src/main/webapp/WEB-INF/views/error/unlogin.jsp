<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title>未登录，无法访问</title>
</head>

<body>
	<h2>未登录，请登录后再访问。</h2>
	<p><a href="javascript:history.go(-1);">返回</a></p>
</body>
</html>
