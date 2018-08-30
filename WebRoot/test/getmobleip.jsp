<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/commons/wap/global_wap.jsp" %>
<!DOCTYPE html>
<head>
<%@ include file="/commons/wap/basejs_wap.jsp" %>
<meta charset="utf-8">
<meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<meta name="apple-touch-fullscreen" content="yes" />
<link rel="stylesheet" href="${path}/view/wap/css/style.css">
<script language="JavaScript">
$(document).ready(function(){
//通过调用新浪IP地址库接口查询用户当前所在国家、省份、城市、运营商信息
$.getScript('http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js',function(){
  $(".country").html(remote_ip_info.country);
  $(".province").html(remote_ip_info.province);
  $(".city").html(remote_ip_info.city);
  $(".isp").html(remote_ip_info.isp);
});

//通过调用QQIP地址库接口查询本机当前的IP地址
$.getScript('http://fw.qq.com/ipaddress',function(){
  $(".ip").html(IPData[0]); 
  });
});
</script>
<title>接品获取IP地址</title>
</head>
<body>
	<div>国家：<span class="country"></span></div>
	<div>省份：<span class="province"></span></div>
	<div>城市：<span class="city"></span></div>
	<div>IP地址：<span class="ip"></span></div>
	<div>运营商：<span class="isp"></span></div>
</body>
</html>
