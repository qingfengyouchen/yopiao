<%@ page contentType="text/html;charset=UTF-8" %>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" name="viewport" id="viewport" >
      <meta name="format-detection" content="telephone=no">
      <meta http-equiv="x-rim-auto-match" content="none">
      <title>立即报名</title>
      <link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/style.css" />
      <link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/global.css" />
      <link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/index.css" />
      <link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/all.css" />
      <link rel="stylesheet" type="text/css" href="${ctx }/static/service/css/demo.css" /> 
</head>
    <body>
       <div class="max-box" style="background:#EFEFF4;">
          <!-- 头部结束 -->
          <div class="main">
            <div class="wraper">
              <form class="registerform" action="${ctx }/service/activity/updateAmount/${id}">
                  <ul>
                    <li>
                      <input type="text" value="" name="trueName" class="inputxt" datatype="s2-10" nullmsg="请输入姓名！" errormsg="姓名至少2个字符,最多10个字符！"/>
                      <p class="Validform_checktip">昵称为2~10个字符</p>
                    </li> 
                    <li>
                     <input type="text" value="" name="mobileNo" class="inputxt"  datatype="m" nullmsg="请输入手机号码！"  errormsg="手机号码格式不对！"/>
                     <p class="Validform_checktip">请输入手机号码！</p>
                    </li>
                    <li class="btn">
                       <input type="submit" value="提 交" />
                    </li>
                  </ul>
                </form>
            </div>
</div>
       </div>
<script type="text/javascript" src="${ctx }/static/service/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ctx }/static/service/js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="${ctx }/static/service/js/jquery-input/jquery.placeholder.min.js"></script>
<script type="text/javascript">
$(function(){
  //$(".registerform").Validform();  //就这一行代码！;
    
  $(".registerform").Validform({
  	tiptype:3,
	ajaxPost:true,
	callback:function(data){
		$.Hidemsg();
		$.Showmsg(data.result);
	}
  });
})

</script>
</body>

</html>