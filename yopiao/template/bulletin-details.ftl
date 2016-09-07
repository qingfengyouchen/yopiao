
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
        <meta name="format-detection" content="telephone=no">
        <meta http-equiv="x-rim-auto-match" content="none"> 
        <title>E购活动</title>
        <script type="text/javascript" src="${ctx}/static/service/js/jquery-1.9.1.min.js"></script>
        <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/style.css" />
        <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/global.css" />
        <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/index.css" />
    </head>
    <body>
       <div class="max-box" style="background:#EFEFF4;">
          <!-- 头部结束 -->
          <div class="main">
             <div class="pic_d">
               <div class="pic">
               	<img src="${activityRootUri}/${activity.imgUrl}">
               </div>
             </div>
             <div class="info">
                <dl>
                  <dt class="largest text-heavy plr">${activity.title}</dt>
                  <dd class="large_normal text-low2 plr">时间：${activity.activityTime?string("yyyy-MM-dd HH:mm")} &nbsp;&nbsp;人次：<span id="has">${activity.hasJoinAmount}</span>/${activity.maxJoinAmount}</dd>
                  <dd class="large_normal text-low2 plr">地址：${activity.address}</dd>
                  <dd class="plr"><a href="${ctx}/service/activity/enroll/${activity.id}" id="enroll" target="_self" class="enlist primary largest_plus">立即报名</a></dd>
                </dl>
             </div>
             <div class="info2">
                <h5 class="largest_plus text-low2"><ins class="ins"></ins>简介</h5>
                ${activity.content}
             </div>
          </div>
       </div>
    </body>
    <script type="text/javascript">
    	$(function(){
    		var id=${activity.id}
    		$.get("${ctx}/service/activity/hasJoinAmount",{id:id},function(data){
    			var sta=data.state;
    			switch(sta){
					case(1): 
						$("#has").text(data.result);
					break;
					default:
						$("#enroll").attr("href","javaScript:;");
						$("#enroll").text(data.result);
				}
    		});
    	});
    </script>
</html>