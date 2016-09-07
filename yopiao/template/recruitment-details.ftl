<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
        <meta name="format-detection" content="telephone=no">
        <meta http-equiv="x-rim-auto-match" content="none"> 
       	<title>招聘</title>
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
                  <dt class="largest blank plr">${recruitment.title}</dt>
                  <dd class="large text-low2 plr"><strong class="plus primary">${recruitment.salary}</strong>元/月</dd>
                  <dd class="normal text-low2 plr">${recruitment.editTime?string("yyyy-MM-dd")}<b>执照已认证</b></dd>
                </dl>
             </div>
             <div class="info">
                <dl>
                  <dd class="large_p text-low2 plr">地址<em class="text-normal large_p">${recruitment.address}</em></dd> 
                  <dd class="large_p text-low2 plr">职位<em class="text-normal large_p">${recruitment.position}</em></dd>
                  <dd class="large_p text-low2 plr">要求<em class="text-normal large_p">${recruitment.requirement}</em></dd> 
                </dl>
             </div>
             <div class="info2">
                <h5 class="largest_plus text-low2"><ins class="ins"></ins>公司介绍</h5>
                <p class="larger text-normal">${recruitment.companyIntroduce}</p>
             </div>
             <div class="info2">
                <h5 class="largest_plus text-low2"><ins class="ins"></ins>职位介绍</h5>
                ${recruitment.content}
             </div>
             <div class="k-z"></div>
             <div class="tel largest">
                <a  href="tel://${recruitment.tel}" class="text-f"><del></del>
                拨打电话</a>
             </div>
          </div>
       </div>
    </body>
</html>