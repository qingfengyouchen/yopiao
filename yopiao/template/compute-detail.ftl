<!DOCTYPE html>
<html>
<head>
    <title>计算结果</title>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"
          name="viewport" id="viewport">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/style.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/global.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/static/service/css/index.css">
    <link href="${ctx}/static/service/css/common.css" rel="stylesheet">
    <link href="${ctx}/static/service/css/detail.css" rel="stylesheet">
    <script type="text/javascript" src="${ctx}/static/service/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/static/service/js/pro.js"></script>
    <#setting number_format="#">
</head>
<body>
<div class="max-box">
    <!-- 头部结束 -->
    <div class="content">
        <div class="c_top line2">
            <div class="top_inner">
                <h4 class="large text-f lr">计算公式</h4>

                <p class="large_normal text-f lr">[数值A÷商品所需人次]取余数+10000001</p>
            </div>
        </div>
        <div class="numA line2">
            <div class="numA_a lr">
                <h4 class="large text-heavy">数值A</h4>

                <p class="large text-low2">=截止该商品开奖时间点前50条全站参与记录</p>

                <p class="large text-low2">=<b class="pink">${goodsTimes.avalue}</b><em class="open blur">展开
                    <ins></ins>
                </em></p>
            </div>
            <div class="m-calc-list">
                <table class="m-calc-resultList" cellpadding="0" cellspacing="0">
                    <thead>
                    <tr>
                        <th class="time">夺宝时间</th>
                        <th class="user">用户帐号</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list buyRecordList as record>
                        <tr class="calcRow">
                            <td class="time">${record.snatchTimeStr} <i class="ico ico-arrow-transfer"></i> <b class="pink">${record.timeValue}</b>
                            </td>
                            <td class="user">
                                <div class="f-breakword"><b class="goUserPage">${record.userNickName}</b></div>
                            </td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div>
        <!--
        <div class="numA line2">
            <div class="numA_a lr">
                <h4 class="large text-heavy">数值B</h4>

                <p class="large text-low2">=最近一期中国福利彩票"老时时彩" 的开奖结果</p>

                <p class="large text-low2">=<b class="pink">${goodsTimes.bvalue}</b>
                    <strong>（第20${goodsTimes.cqsscPeriodNo}期）</strong>
                    <a href="http://caipiao.163.com/award/cqssc/${cqsscDateYYYYMMDD}.html"class="blur">开奖查询</a>
                </p>
            </div>
        </div>
        -->
        <div class="numB line2 plr">
            <h4 class="large text-heavy">计算结果</h4>
            <p class="large text-low2">幸运号码：<b class="pink">${goodsTimes.luckNum}</b></p>
        </div>
    </div>
</div>

</body>
</html>