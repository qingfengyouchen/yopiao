<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
        <meta name="format-detection" content="telephone=no">
        <meta http-equiv="x-rim-auto-match" content="none"> 
        <title>E购八卦</title>
        <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/style.css" />
        <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/global.css" />
        <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/index.css" />
    	
    </head>
    <body>
       <div class="max-box" style="background:#EFEFF4;">
          <div class="main">
             <div class="info">
                <dl>
                  <dt class="largest blank plr">${entity.title}</dt>
                  <dd class="plr text-low2"><b class="large">${entity.createTime?string("yyyy-MM-dd")}</b></dd>
                </dl>
             </div>
             <div class="info2">
                <h5 class="largest_plus text-low2"><ins class="ins"></ins>简介</h5>
                <p class="largest_plus text-normal">${entity.content}</p>
             </div>
             <div class="k-z"></div>
          </div>
       </div>
    </body>
</html>