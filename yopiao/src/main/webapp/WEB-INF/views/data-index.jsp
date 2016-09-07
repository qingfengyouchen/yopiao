<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>统计也没</title>
  <link href="${ctx}/static/mould/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
  <link href="${ctx}/static/mould/css/font-awesome.css?v=4.3.0" rel="stylesheet">
  <!-- Morris -->
  <link href="${ctx}/static/mould/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">
  <!-- Gritter -->
  <link href="${ctx}/static/mould/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
  <link href="${ctx}/static/mould/css/animate.css" rel="stylesheet">
  <link href="${ctx}/static/mould/css/style.css?v=3.2.0" rel="stylesheet">

</head>

<body class="gray-bg">
<div class="wrapper wrapper-content">
  <div class="row">
    <div class="col-sm-3">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <span class="label label-success pull-right">月</span>
          <h5>总收入</h5>
        </div>
        <div class="ibox-content">
          <h1 class="no-margins">40,886,200</h1>
          <%--<div class="stat-percent font-bold text-success">98% <i class="fa fa-bolt"></i>
          </div>
          <small>总收入</small>--%>
        </div>
      </div>
    </div>
    <div class="col-sm-3">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <span class="label label-info pull-right">全年</span>
          <h5>销量（开奖次数）</h5>
        </div>
        <div class="ibox-content">
          <h1 class="no-margins">275,800</h1>
          <%--<div class="stat-percent font-bold text-info">20% <i class="fa fa-level-up"></i>
          </div>
          <small>新订单</small>--%>
        </div>
      </div>
    </div>
    <div class="col-sm-3">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <span class="label label-primary pull-right">今天</span>
          <h5>访客</h5>
        </div>
        <div class="ibox-content">
          <h1 class="no-margins">106,120</h1>
          <%--<div class="stat-percent font-bold text-navy">44% <i class="fa fa-level-up"></i>
          </div>
          <small>新访客</small>--%>
        </div>
      </div>
    </div>
    <div class="col-sm-3">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <span class="label label-danger pull-right">最近一个月</span>
          <h5>活跃用户</h5>
        </div>
        <div class="ibox-content">
          <h1 class="no-margins">80,600</h1>
          <%--<div class="stat-percent font-bold text-danger">38% <i class="fa fa-level-down"></i>
          </div>
          <small>1月</small>--%>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
    <div class="col-sm-12">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>订单</h5>
          <div class="pull-right">
            <div class="btn-group">
              <button type="button" class="btn btn-xs btn-white active">天</button>
              <button type="button" class="btn btn-xs btn-white">月</button>
              <button type="button" class="btn btn-xs btn-white">年</button>
            </div>
          </div>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-sm-12">
              <div class="flot-chart">
                <div class="flot-chart-content" id="flot-dashboard-chart"></div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>


</div>

<!-- 全局js -->
<script src="${ctx}/static/mould/js/jquery-2.1.1.min.js"></script>
<script src="${ctx}/static/mould/js/bootstrap.min.js?v=3.4.0"></script>



<!-- Flot -->
<script src="${ctx}/static/mould/js/plugins/flot/jquery.flot.js"></script>
<script src="${ctx}/static/mould/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script src="${ctx}/static/mould/js/plugins/flot/jquery.flot.spline.js"></script>
<script src="${ctx}/static/mould/js/plugins/flot/jquery.flot.resize.js"></script>
<script src="${ctx}/static/mould/js/plugins/flot/jquery.flot.pie.js"></script>
<script src="${ctx}/static/mould/js/plugins/flot/jquery.flot.symbol.js"></script>

<!-- 自定义js -->
<script src="${ctx}/static/mould/js/content.js?v=1.0.0"></script>


<%--
<!-- jQuery UI -->
<script src="${ctx}/static/mould/js/plugins/jquery-ui/jquery-ui.min.js"></script>

<!-- Jvectormap -->
<script src="${ctx}/static/mould/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="${ctx}/static/mould/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>

<!-- EayPIE -->
<script src="${ctx}/static/mould/js/plugins/easypiechart/jquery.easypiechart.js"></script>

<!-- Sparkline -->
<script src="${ctx}/static/mould/js/plugins/sparkline/jquery.sparkline.min.js"></script>

<!-- Sparkline demo data  -->
<script src="${ctx}/static/mould/js/demo/sparkline-demo.js"></script>
--%>

<script>
  $(document).ready(function () {

    var data2 = [
      [gd(2016, 1, 1), 7], [gd(2016, 1, 2), 6], [gd(2016, 1, 3), 4], [gd(2016, 1, 4), 8],
      [gd(2016, 1, 5), 9], [gd(2016, 1, 6), 7], [gd(2016, 1, 7), 5], [gd(2016, 1, 8), 4],
      [gd(2016, 1, 9), 7], [gd(2016, 1, 10), 8], [gd(2016, 1, 11), 9], [gd(2016, 1, 12), 6],
      [gd(2016, 1, 13), 4], [gd(2016, 1, 14), 5], [gd(2016, 1, 15), 11], [gd(2016, 1, 16), 8],
      [gd(2016, 1, 17), 8], [gd(2016, 1, 18), 11], [gd(2016, 1, 19), 11], [gd(2016, 1, 20), 6],
      [gd(2016, 1, 21), 6], [gd(2016, 1, 22), 8], [gd(2016, 1, 23), 11], [gd(2016, 1, 24), 13],
      [gd(2016, 1, 25), 7], [gd(2016, 1, 26), 9], [gd(2016, 1, 27), 9], [gd(2016, 1, 28), 8],
      [gd(2016, 1, 29), 5], [gd(2016, 1, 30), 8], [gd(2016, 1, 31), 25]
    ];

    var data3 = [
      [gd(2016, 1, 1), 800], [gd(2016, 1, 2), 500], [gd(2016, 1, 3), 600], [gd(2016, 1, 4), 700],
      [gd(2016, 1, 5), 500], [gd(2016, 1, 6), 456], [gd(2016, 1, 7), 800], [gd(2016, 1, 8), 589],
      [gd(2016, 1, 9), 467], [gd(2016, 1, 10), 876], [gd(2016, 1, 11), 689], [gd(2016, 1, 12), 700],
      [gd(2016, 1, 13), 500], [gd(2016, 1, 14), 600], [gd(2016, 1, 15), 700], [gd(2016, 1, 16), 786],
      [gd(2016, 1, 17), 345], [gd(2016, 1, 18), 888], [gd(2016, 1, 19), 888], [gd(2016, 1, 20), 888],
      [gd(2016, 1, 21), 987], [gd(2016, 1, 22), 444], [gd(2016, 1, 23), 999], [gd(2016, 1, 24), 567],
      [gd(2016, 1, 25), 786], [gd(2016, 1, 26), 666], [gd(2016, 1, 27), 888], [gd(2016, 1, 28), 900],
      [gd(2016, 1, 29), 178], [gd(2016, 1, 30), 555], [gd(2016, 1, 31), 993]
    ];

    var dataset = [
      {
        label: "总收入",
        data: data3,
        color: "#1ab394",
        bars: {
          show: true,
          align: "center",
          barWidth: 24 * 60 * 60 * 600,
          lineWidth: 0
        }

      }, {
        label: "实际收入",
        data: data2,
        yaxis: 2,
        color: "#464f88",
        lines: {
          lineWidth: 1,
          show: true,
          fill: true,
          fillColor: {
            colors: [{
              opacity: 0.2
            }, {
              opacity: 0.2
            }]
          }
        },
        splines: {
          show: false,
          tension: 0.6,
          lineWidth: 1,
          fill: 0.1
        }
      }
    ];


    var options = {
      xaxis: {
        mode: "time",
        tickSize: [3, "day"],
        tickLength: 0,
        axisLabel: "Date",
        axisLabelUseCanvas: true,
        axisLabelFontSizePixels: 12,
        axisLabelFontFamily: 'Arial',
        axisLabelPadding: 10,
        color: "#838383"
      },
      yaxes: [
        {
          position: "left",
          max: 1070,
          color: "#838383",
          axisLabelUseCanvas: true,
          axisLabelFontSizePixels: 12,
          axisLabelFontFamily: 'Arial',
          axisLabelPadding: 3
        },
        {
          position: "right",
          clolor: "#838383",
          axisLabelUseCanvas: true,
          axisLabelFontSizePixels: 12,
          axisLabelFontFamily: ' Arial',
          axisLabelPadding: 67
        }
      ],
      legend: {
        noColumns: 1,
        labelBoxBorderColor: "#000000",
        position: "nw"
      },
      grid: {
        hoverable: false,
        borderWidth: 0,
        color: '#838383'
      }
    };

    function gd(year, month, day) {
      return new Date(year, month - 1, day).getTime();
    }

    $.plot($("#flot-dashboard-chart"), dataset, options);

  });
</script>

</body>
</html>