<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
        <meta name="format-detection" content="telephone=no">
        <meta http-equiv="x-rim-auto-match" content="none"> 
        <title>顺风代驾</title>
        <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/style.css" />
        <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/global.css" />
        <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/index.css" />
    </head>
    <body>
       <div class="max-box" style="background:#EFEFF4;">
          <!-- 头部结束 -->
          <div class="main">
             <div class="info">
                <dl>
                  <dt class="largest blank plr">${delegateDrive.title}</dt>
                  <dd class="large text-low2 plr">联系人：${delegateDrive.linker}<b>${delegateDrive.editTime?string("yyyy-MM-dd")}</b></dd>
                </dl>
             </div>
             <div class="info2">
                <h5 class="largest_plus text-low2"><ins class="ins"></ins>简介</h5>
                ${delegateDrive.content}
             </div>
             <div class="k-z"></div>
             <div class="tel largest">
                <a  href="tel://${delegateDrive.tel}" class="text-f"><del></del>
                拨打电话</a>
             </div>
          </div>
       </div>
    </body>
</html>