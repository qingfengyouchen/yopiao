<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
    <meta http-equiv="x-rim-auto-match" content="none">
    <title>图文详情</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/global.css" />
</head>

<body>
<#list imageList as entity>
<img src="${imgRootUri}/${entity.url}" style="width: 100%;display: block;">
</#list>
<#list detailImageUrlList as entity2>
<img src="${entity2.url}" style="width: 100%;display: block;">
</#list>
</body>
</html>